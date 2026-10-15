package com.example.embytv.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("emby_tv_prefs")

class EmbyPreferences(private val context: Context) {

    companion object {
        private val SERVER_URL = stringPreferencesKey("server_url")
        private val USER_ID = stringPreferencesKey("user_id")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val USERNAME = stringPreferencesKey("username")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    fun isUserLoggedIn(): Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_LOGGED_IN] ?: false
    }

    fun getServerUrl(): Flow<String?> = context.dataStore.data.map { it[SERVER_URL] }
    fun getUserId(): Flow<String?> = context.dataStore.data.map { it[USER_ID] }
    fun getUsername(): Flow<String?> = context.dataStore.data.map { it[USERNAME] }
    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }

    suspend fun saveCredentials(serverUrl: String, userId: String, token: String, username: String) {
        context.dataStore.edit { prefs ->
            prefs[SERVER_URL] = serverUrl
            prefs[USER_ID] = userId
            prefs[ACCESS_TOKEN] = token
            prefs[USERNAME] = username
            prefs[IS_LOGGED_IN] = true
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
