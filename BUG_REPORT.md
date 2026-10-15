# EmbyTV Bug Report & Fix Plan

## Files Read (27 .kt + 3 config + 2 resources = 32 total)

### BUGS FOUND

1. **BaseItem.kt** — File name mismatch: file is named `BaseItem.kt` but contains `MediaItem` as the main class. Serialization works fine, but naming is misleading. No actual bug here.

2. **build.gradle.kts (root)** — OK, has ksp/hilt plugins declared with `apply false`. Good.

3. **app/build.gradle.kts** — 
   - ✅ KSP used for hilt-compiler and room-compiler. Good.
   - ✅ Retrofit kotlinx-serialization converter included. Good.
   - ❌ MISSING `com.jakewharton.retrofit2:converter-kotlinx-serialization:1.0.0` — ApiModule.kt imports `com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory`, but only `retrofit2-converter-kotlinx-serialization:2.11.0` is listed. The version should be `1.0.0` from `jakewharton`, not from `retrofit2` coordinates. Need to check which coordinate is correct. Actually `retrofit2-converter-kotlinx-serialization:2.11.0` might be different from `com.jakewharton.retrofit2:retrofit2-kotlinx-serialization-converter:1.0.0`. Let me check this carefully.
   
   Actually, looking at the dependency list, it has `retrofit2-converter-kotlinx-serialization:2.11.0` which IS the right artifact from Square. But the import in ApiModule uses `com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory` which comes from Jake Wharton's library (`com.jakewharton.retrofit2:retrofit2-kotlinx-serialization-converter`). These are DIFFERENT libraries with different package names. Need to add the correct dependency.

4. **EmbyApiService.kt** — 
   - `getItemPoster` returns `ResponseBody` but no @Headers needed since token comes from OkHttp interceptor. OK.
   - The `@Query("quality") quality: Int = 90` won't work with path-style queries like `Images/Primary?quality={quality}` — quality should use `@Path("quality")`. This is a BUG.

5. **EmbyRepository.kt** — 
   - `searchItems()` returns empty ResultPage always (stub). OK for scaffolding.
   - `getPlaybackInfo()` hardcodes `audioStreamIndex = 1` — could be 0 or null. Minor issue.
   - `buildImageUrl` uses `UUID.randomUUID().toString().take(8)` as tag — should ideally use the `primaryImageTag` field instead for cache control. Functional but inefficient.

6. **ApiModule.kt** —
   - AuthInterceptor is a NO-OP (doesn't inject token). Major bug — auth will fail.
   - Base URL is hardcoded placeholder `https://localhost:8920/`. Needs to be dynamic per connection.
   - Missing `retrofit2-kotlinx-serialization-converter` dependency from jakewharton.

7. **LoginViewModel.kt** — Has duplicate `package` statement at top (written twice). CRITICAL BUG.

8. **PlayerScreen.kt** — Has duplicate `package com.example.embytv.ui.player` at top. CRITICAL BUG.

9. **LoginScreen.kt** — `viewModel.authenticate(serverUrl, username, password)` but LoginViewModel method is called `authenticate()`. Wait — let me verify... Yes, after my earlier edits it should be correct.

10. **SettingsScreen.kt** — Uses `ListItem` and `Divider` from material3 without importing them. COMPILATION ERROR.

11. **HomeScreen.kt** — Imports `LazyHorizontalGrid` but doesn't use it. Unused import. Not a compilation error.

12. **AndroidManifest.xml** — application tag already has `android:name=".EmbyTVApplication"`. Good.

13. **Theme.kt** — Color scheme uses purple/pink which is fine for dark theme. OK.

14. **SearchScreen.kt** — SearchViewModel parameter is nullable `SearchViewModel? = null` but never injected by Hilt properly. OK for scaffolding.

15. **settings.gradle.kts** — Duplicate `dependencyResolutionManagement` block. One inside `pluginManagement` scope incorrectly? Let me check... Actually they're both top-level blocks, which Gradle allows. But `repositoriesMode` appears twice which is redundant. Not a compilation bug.
