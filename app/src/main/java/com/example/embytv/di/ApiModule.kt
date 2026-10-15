package com.example.embytv.di

import com.example.embytv.data.api.EmbyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    /** Token holder — updated dynamically after login; accessed synchronously from interceptors. */
    object AuthHolder {
        var token: String? = null
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        preferences: com.example.embytv.data.preferences.EmbyPreferences
    ): Interceptor = Interceptor { chain ->
        val request = chain.request()
        val urlPath = request.url.encodedPath

        // Skip auth header for System/Info/Public and AuthenticateByName endpoints
        if (!urlPath.contains("System/Info/Public") && !urlPath.contains("AuthenticateByName")) {
            // Use current token from holder first, fall back to async read from DataStore
            val currentToken = AuthHolder.token ?: run { 
                // Note: This should ideally be handled via a coroutine-based approach
                // For now, we rely on AuthHolder.token being set after login
                null
            }
            if (currentToken != null) {
                val authenticated = request.newBuilder()
                    .addHeader("X-Emby-Token", currentToken)
                    .build()
                return@Interceptor chain.proceed(authenticated)
            }
        }

        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://localhost:8920/") // placeholder; overridden at runtime via repository.connect()
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): EmbyApiService =
        retrofit.create(EmbyApiService::class.java)
}
