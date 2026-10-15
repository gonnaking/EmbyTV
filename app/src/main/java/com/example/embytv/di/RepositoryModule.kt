package com.example.embytv.di

import com.example.embytv.data.EmbyRepository
import com.example.embytv.data.api.EmbyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEmbyRepository(apiService: EmbyApiService): EmbyRepository =
        EmbyRepository(apiService)
}
