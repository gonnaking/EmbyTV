package com.example.embytv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embytv.data.EmbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EpisodesUiState {
    object Loading : EpisodesUiState
    data class Loaded(
        val episodes: List<com.example.embytv.data.model.MediaItem>,
        val seasonName: String
    ) : EpisodesUiState
    data class Error(val message: String) : EpisodesUiState
}

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: EmbyRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EpisodesUiState>(EpisodesUiState.Loading)
    val state: StateFlow<EpisodesUiState> = _state

    fun loadEpisodes(seasonId: String) {
        viewModelScope.launch {
            try {
                val userId = repository.userId ?: ""
                if (userId.isEmpty()) {
                    _state.value = EpisodesUiState.Error("Not logged in")
                    return@launch
                }
                
                // Get season details first to get series ID
                val seasonDetails = repository.getItemDetails(seasonId)
                val seriesId = seasonDetails?.seriesId ?: seasonDetails?.parentId ?: ""
                
                if (seriesId.isNotEmpty()) {
                    val page = repository.getSeasonEpisodes(seriesId, userId, seasonId)
                    _state.value = EpisodesUiState.Loaded(
                        episodes = page.items,
                        seasonName = seasonDetails?.name ?: "Episodes"
                    )
                } else {
                    _state.value = EpisodesUiState.Error("Could not find series")
                }
            } catch (e: Exception) {
                _state.value = EpisodesUiState.Error(e.message ?: "Network error")
            }
        }
    }
}
