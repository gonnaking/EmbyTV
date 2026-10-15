package com.example.embytv.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.embytv.ui.LocalServerUrl
import com.example.embytv.viewmodel.DetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: String,
    onBack: () -> Unit,
    onPlayClick: (String) -> Unit,
    onSeriesClick: (String) -> Unit,
    viewModel: com.example.embytv.viewmodel.DetailViewModel = hiltNavGraphViewModel()
) {
    val serverUrl by remember { mutableStateOf(LocalServerUrl.current) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.loadDetails(itemId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("影片详情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is DetailUiState.Loading -> {
                Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is DetailUiState.Loaded -> {
                ItemContent(
                    item = s.item,
                    modifier = Modifier.padding(padding),
                    onPlayClick = {
                        if (s.item.mediaSources != null && s.item.mediaSources!!.isNotEmpty()) {
                            onPlayClick(s.item.id)
                        }
                    },
                    onSeriesClick = { seriesId ->
                        if (s.item.isSeries && !seriesId.isNullOrEmpty()) {
                            onSeriesClick(seriesId)
                        } else if (!s.item.seriesId.isNullOrEmpty()) {
                            onSeriesClick(s.item.seriesId!!)
                        }
                    },
                    serverBaseUrl = serverUrl
                )
            }

            is DetailUiState.Error -> {
                Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.Center) {
                    Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun ItemContent(
    item: com.example.embytv.data.model.MediaItem,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    onSeriesClick: () -> Unit,
    serverBaseUrl: String = ""
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Poster image
        Image(
            painter = rememberAsyncImagePainter(
                model = com.example.embytv.data.EmbyRepository.buildImageUrl(
                    serverBaseUrl = serverBaseUrl,
                    itemId = item.id,
                    imageType = "Primary"
                )
            ),
            contentDescription = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.67f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title and info
        Text(item.name, style = MaterialTheme.typography.headlineMedium)

        item.productionYear?.let {
            Text(it.toString(), style = MaterialTheme.typography.bodyLarge)
        }

        item.communityRating?.let { rating ->
            Text(String.format("%.1f", rating), style = MaterialTheme.typography.bodyMedium)
        }

        item.genres?.let { genres ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                genres.forEach { genre ->
                    Badge {
                        Text(genre, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Overview
        item.overview?.let { overview ->
            Text(
                text = overview,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        // People/Cast
        item.people?.let { people ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Cast", style = MaterialTheme.typography.titleMedium)
                    people.take(10).forEach { person ->
                        Text("- ${person.name}${person.role?.let { " ($it)" } ?: ""}")
                    }
                }
            }
        }

        // Play button
        Button(
            onClick = onPlayClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Icon(Icons.Filled.PlayArrow, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("播放")
        }
    }
}
