package com.example.embytv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.embytv.data.model.MediaItem
import com.example.embytv.viewmodel.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: com.example.embytv.viewmodel.HomeViewModel = hiltNavGraphViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EmbyTV") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (uiState) {
                is HomeUiState.Idle -> {
                    Text("Loading...", modifier = Modifier.align(Alignment.Center))
                }
                
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }
                
                is HomeUiState.Success -> {
                    val state = uiState as HomeUiState.Success
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (state.continueWatching.isNotEmpty()) {
                            item {
                                ContinueWatchingSection(
                                    items = state.continueWatching,
                                    onItemClick = onItemClick
                                )
                            }
                        }
                        
                        if (state.latestAdded.isNotEmpty()) {
                            item {
                                LatestAddedSection(
                                    items = state.latestAdded,
                                    onItemClick = onItemClick
                                )
                            }
                        }
                        
                        item {
                            MoviesSection(
                                items = state.movies,
                                onItemClick = onItemClick
                            )
                        }
                        
                        item {
                            SeriesSection(
                                items = state.series,
                                onItemClick = onItemClick
                            )
                        }
                    }
                }
                
                is HomeUiState.Error -> {
                    val error = (uiState as HomeUiState.Error).message
                    Text(
                        "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieCard(item: MediaItem, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick(item.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                        model = com.example.embytv.data.EmbyRepository.buildImageUrl(
                            serverBaseUrl = com.example.embytv.ui.LocalServerUrl.current,
                        itemId = item.id,
                        imageType = "Primary"
                    )
                ),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Text(
                text = item.name,
                modifier = Modifier.padding(8.dp),
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ContinueWatchingSection(items: List<MediaItem>, onItemClick: (String) -> Unit) {
    SectionHeader(title = "继续观看")
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(items) { item ->
            Spacer(modifier = Modifier.width(16.dp))
            MovieCard(item = item, onClick = onItemClick)
        }
    }
}

@Composable
private fun LatestAddedSection(items: List<MediaItem>, onItemClick: (String) -> Unit) {
    SectionHeader(title = "最新添加")
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(items) { item ->
            Spacer(modifier = Modifier.width(16.dp))
            MovieCard(item = item, onClick = onItemClick)
        }
    }
}

@Composable
private fun MoviesSection(items: List<MediaItem>, onItemClick: (String) -> Unit) {
    SectionHeader(title = "电影")
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(items) { item ->
            Spacer(modifier = Modifier.width(16.dp))
            MovieCard(item = item, onClick = onItemClick)
        }
    }
}

@Composable
private fun SeriesSection(items: List<MediaItem>, onItemClick: (String) -> Unit) {
    SectionHeader(title = "剧集")
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(items) { item ->
            Spacer(modifier = Modifier.width(16.dp))
            MovieCard(item = item, onClick = onItemClick)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
