package com.example.embytv.di

import android.content.Context
import com.example.embytv.data.preferences.EmbyPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideEmbyPreferences(@ApplicationContext context: Context): EmbyPreferences =
        EmbyPreferences(context)
}
