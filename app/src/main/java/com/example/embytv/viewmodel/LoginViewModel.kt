package com.example.embytv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embytv.data.EmbyRepository
import com.example.embytv.data.preferences.EmbyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data class Loading(val stage: Stage) : LoginUiState {
        enum class Stage { CONNECTING, LOGIN }
    }
    data class Success(val userId: String, val serverName: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: EmbyRepository,
    private val preferences: EmbyPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun authenticate(serverUrl: String, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading(LoginUiState.Loading.Stage.CONNECTING)
            
            // Step 1: Connect to server
            try {
                val systemInfo = repository.connect(serverUrl)
                if (systemInfo == null) {
                    _uiState.value = LoginUiState.Error("Failed to connect to server")
                    return@launch
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Connection error")
                return@launch
            }

            // Step 2: Authenticate user
            _uiState.value = LoginUiState.Loading(LoginUiState.Loading.Stage.LOGIN)
            try {
                val result = repository.login(username, password)
                if (result?.accessToken != null) {
                    preferences.saveCredentials(
                        serverUrl = repository.serverUrl ?: "",
                        userId = result.user?.id ?: "",
                        token = result.accessToken,
                        username = username
                    )
                    _uiState.value = LoginUiState.Success(
                        userId = result.user?.id ?: "",
                        serverName = systemInfo?.serverName ?: ""
                    )
                } else {
                    _uiState.value = LoginUiState.Error("Authentication failed")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Login error")
            }
        }
    }
}
