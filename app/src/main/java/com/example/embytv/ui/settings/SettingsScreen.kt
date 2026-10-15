package com.example.embytv.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ListItem(
                headlineContent = { Text("服务器信息") },
                trailingContent = { Icon(Icons.Default.Settings, null) },
                onClick = {
                    // TODO: Navigate to server configuration screen
                }
            )
            
            Divider()
            
            ListItem(
                headlineContent = { Text("偏好设置") },
                trailingContent = { Icon(Icons.Default.Settings, null) },
                onClick = {
                    // TODO: Navigate to preferences screen
                }
            )
            
            Divider()
            
            if (onLogout != null) {
                ListItem(
                    headlineContent = { Text("退出登录") },
                    trailingContent = { Icon(Icons.Default.ExitToApp, null, tint = MaterialTheme.colorScheme.error) },
                    onClick = onLogout
                )
            }
        }
    }
}
