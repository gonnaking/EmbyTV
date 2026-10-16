package com.example.embytv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embytv.data.EmbyRepository
import com.example.embytv.data.model.MediaItem
import com.example.embytv.data.model.PlaybackResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Loaded(val item: MediaItem) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: EmbyRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state

    var selectedMediaSourceIndex = 0
        private set

    fun loadDetails(itemId: String) {
        viewModelScope.launch {
            try {
                val details = repository.getItemDetails(itemId)
                if (details != null) {
                    _state.value = DetailUiState.Loaded(details)
                    // Auto-select best playback source (DirectPlay > DirectStream > Transcode)
                    val sources = details.mediaSources ?: emptyList()
                    for ((idx, src) in sources.withIndex()) {
                        if (src?.supportsDirectPlay == true || src?.protocol == "Http") {
                            selectedMediaSourceIndex = idx
                            break
                        }
                    }
                } else {
                    _state.value = DetailUiState.Error("Failed to load item details")
                }
            } catch (e: Exception) {
                _state.value = DetailUiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun getPlaybackInfo(itemId: String): kotlinx.coroutines.Deferred<PlaybackResponse?> {
        return viewModelScope.async {
            repository.getPlaybackInfo(itemId)
        }
    }

    fun onMediaSourceSelected(index: Int) {
        selectedMediaSourceIndex = index
    }
}
