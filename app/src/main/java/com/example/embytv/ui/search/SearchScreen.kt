package com.example.embytv.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onItemClick: (String) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: com.example.embytv.viewmodel.SearchViewModel = hiltNavGraphViewModel()
) {
    var query by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            viewModel.searchItems(query)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("搜索") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; viewModel.searchItems(query) },
                placeholder = { Text("输入关键词搜索...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth()
            )
            
            when (uiState) {
                is com.example.embytv.viewmodel.SearchUiState.Loading -> {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                
                is com.example.embytv.viewmodel.SearchUiState.Success -> {
                    val results = (uiState as com.example.embytv.viewmodel.SearchUiState.Success).results
                    if (results.isNotEmpty()) {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(results.size) { idx ->
                                val item = results[idx]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                        .clickable { onItemClick(item.id) },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    } else if (query.isNotBlank()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("未找到结果: $query")
                        }
                    }
                }

                is com.example.embytv.viewmodel.SearchUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${(uiState as com.example.embytv.viewmodel.SearchUiState.Error).message}",
                            color = MaterialTheme.colorScheme.error)
                    }
                }

                SearchUiState.Idle -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("输入关键词开始搜索")
                    }
                }
            }
        }
    }
}
