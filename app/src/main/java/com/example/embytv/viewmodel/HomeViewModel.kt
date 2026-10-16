package com.example.embytv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embytv.data.EmbyRepository
import com.example.embytv.data.model.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object Loading : HomeUiState
    data class Success(
        val continueWatching: List<MediaItem>,
        val latestAdded: List<MediaItem>,
        val movies: List<MediaItem>,
        val series: List<MediaItem>
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EmbyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> get() = _uiState

    fun loadLibrary(userId: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val continueWatchingJob = async { repository.getContinueWatching(userId) }
                val latestAddedJob = async { repository.getLatestItems(userId, limit = 12) }
                val moviesJob = async { repository.getUserLibrary(userId).items.take(30) }
                val seriesJob = async {
                    val library = repository.getUserLibrary(userId)
                    library.items.filter { it.type == "Series" }.take(30)
                }

                _uiState.value = HomeUiState.Success(
                    continueWatching = continueWatchingJob.await().items,
                    latestAdded = latestAddedJob.await(),
                    movies = moviesJob.await(),
                    series = seriesJob.await()
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to load library")
            }
        }
    }
}
