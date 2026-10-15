package com.example.embytv.ui

import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocal providing the current Emby server base URL (e.g. "https://192.168.1.100:8096/")
 * across all screens. Set once in AppNavHost; reads from shared preferences automatically.
 */
val LocalServerUrl = compositionLocalOf<String> { "" }
