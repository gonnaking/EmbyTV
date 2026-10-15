package com.example.embytv.ui.episodes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.embytv.viewmodel.EpisodesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodesScreen(
    seasonId: String,
    onEpisodeClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: com.example.embytv.viewmodel.EpisodesViewModel = hiltNavGraphViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(seasonId) {
        viewModel.loadEpisodes(seasonId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("剧集") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is EpisodesUiState.Loading -> {
                Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is EpisodesUiState.Loaded -> {
                Column(modifier = Modifier.padding(padding)) {
                    if (s.episodes.isNotEmpty()) {
                        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                            items(s.episodes.size) { idx ->
                                val episode = s.episodes[idx]
                                EpisodeItemRow(
                                    episode = episode,
                                    onClick = { onEpisodeClick(episode.id) }
                                )
                                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No episodes found")
                        }
                    }
                }
            }

            is EpisodesUiState.Error -> {
                Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.Center) {
                    Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun EpisodeItemRow(
    episode: com.example.embytv.data.model.MediaItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thumbnail
        Card(
            modifier = Modifier
                .sizeIn(width = 160.dp, height = 90.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                                model = com.example.embytv.data.EmbyRepository.buildImageUrl(
                                    serverBaseUrl = com.example.embytv.ui.LocalServerUrl.current,
                        itemId = episode.id,
                        imageType = "Primary"
                    )
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            
            // Play overlay
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // Episode info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${episode.indexNumber?.toString()?.plus(" ")}${episode.name}",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
            
            episode.overview?.let { overview ->
                Text(
                    text = overview.take(100) + if (overview.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } ?: run {
                Text(
                    text = "E${episode.indexNumber ?: "?"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
