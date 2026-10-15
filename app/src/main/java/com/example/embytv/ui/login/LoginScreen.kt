package com.example.embytv.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.embytv.data.preferences.EmbyPreferences
import com.example.embytv.viewmodel.LoginUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: com.example.embytv.viewmodel.LoginViewModel = hiltNavGraphViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var serverUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    // Restore saved credentials on composition start
    LaunchedEffect(Unit) {
        val prefs = EmbyPreferences(androidx.compose.ui.platform.LocalContext.current.applicationContext)
        prefs.getServerUrl().collect { url ->
            if (url != null && serverUrl.isEmpty()) {
                serverUrl = url
            }
        }
        prefs.getUsername().collect { user ->
            if (user != null && username.isEmpty()) {
                username = user
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is com.example.embytv.viewmodel.LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EmbyTV") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Emby TV Client", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = serverUrl,
                onValueChange = { serverUrl = it },
                label = { Text("Server URL") },
                placeholder = { Text("http://192.168.1.100:8096") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.authenticate(serverUrl, username, password)
                },
                enabled = uiState !is com.example.embytv.viewmodel.LoginUiState.Loading &&
                          serverUrl.isNotBlank() && username.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is com.example.embytv.viewmodel.LoginUiState.Loading) {
                    val stage = (uiState as com.example.embytv.viewmodel.LoginUiState).stage
                    Text(if (stage == LoginUiState.Loading.Stage.CONNECTING) "Connecting..." else "Logging in...")
                } else {
                    Text("Connect & Login")
                }
            }

            when (uiState) {
                is com.example.embytv.viewmodel.LoginUiState.Error ->
                    Text((uiState as com.example.embytv.viewmodel.LoginUiState.Error).message, color = MaterialTheme.colorScheme.error)

                is com.example.embytv.viewmodel.LoginUiState.Success ->
                    Text("Connected! Redirecting...", color = MaterialTheme.colorScheme.primary)

                else -> {}
            }
        }
    }
}
