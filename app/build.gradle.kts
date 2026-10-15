plugins {
    id("com.android.application") version "8.5.2"
    id("org.jetbrains.kotlin.android") version "1.9.24"
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    id("com.google.dagger.hilt.android") version "2.51.1"
}

android {
    namespace = "com.example.embytv"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.embytv"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }

    buildTypes {
        release { isMinifyEnabled = false }
        debug { isDebuggable = true }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    // Force all androidx.compose artifacts to 1.6.8.
    // androidx.tv transitively pulls Compose UI 1.7.x whose
    // Compose Compiler 1.3.2 is incompatible with Kotlin 1.9.24.
    implementation("androidx.compose.ui:ui:1.6.8") { because("Compose Compiler compat with Kotlin 1.9.24") }
    constraints {
        implementation("androidx.compose.compiler:compiler:1.5.14") {
            because("Kotlin 1.9.24 compatible Compose Compiler")
        }
    }
    configurations.all {
        resolutionStrategy {
            force("androidx.compose.ui:ui:1.6.8")
            force("androidx.compose.material3:material3:1.2.1")
            force("androidx.compose.foundation:foundation:1.6.8")
            force("androidx.compose.runtime:runtime:1.6.8")
            force("androidx.compose.animation:animation:1.6.8")
            force("androidx.compose.ui:ui-tooling:1.6.8")
            force("androidx.compose.ui:ui-tooling-preview:1.6.8")
            force("androidx.compose.ui:ui-graphics:1.6.8")
            force("androidx.compose.runtime:runtime-livedata:1.6.8")
            force("androidx.compose.runtime:runtime-rxjava2:1.6.8")
            force("androidx.compose.runtime:runtime-rxjava3:1.6.8")
            force("androidx.compose.runtime:runtime-saveable:1.6.8")
        }
    }

    // Compose + TV
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.6.8")
    implementation("androidx.tv:tv-foundation:1.0.0-alpha12")
    implementation("androidx.tv:tv-material:1.0.0-beta01")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")

    // Lifecycle / ViewModel / Activity
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Coroutines + Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Networking — Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Image loading — Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Playback — Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.5.0")
    implementation("androidx.media3:media3-ui:1.5.0")
    implementation("androidx.media3:media3-session:1.5.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Local storage
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // DI — Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
}
