package com.example.embytv.ui.series

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.embytv.viewmodel.SeriesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesScreen(
    seriesId: String,
    onSeasonClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: com.example.embytv.viewmodel.SeriesViewModel = hiltNavGraphViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(seriesId) {
        viewModel.loadDetails(seriesId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("剧集详情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is SeriesUiState.Loading -> {
                Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SeriesUiState.Loaded -> {
                Column(modifier = Modifier.padding(padding)) {
                    Text(s.seriesName, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 16.dp))
                    
                    s.overview?.let { overview ->
                        Text(overview, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp))
                    }

                    if (s.seasons.isNotEmpty()) {
                        Text("季", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                        
                        LazyColumn {
                            items(s.seasons.size) { idx ->
                                val season = s.seasons[idx]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                        .clickable { onSeasonClick(season.id) },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("第${season.indexNumber ?: idx + 1}季", style = MaterialTheme.typography.titleMedium)
                                        Icon(Icons.Default.ChevronRight, null)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is SeriesUiState.Error -> {
                Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.Center) {
                    Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
