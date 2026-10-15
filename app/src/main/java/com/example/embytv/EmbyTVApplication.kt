package com.example.embytv

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point with Hilt dependency injection container initialization.
 */
@HiltAndroidApp
class EmbyTVApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
