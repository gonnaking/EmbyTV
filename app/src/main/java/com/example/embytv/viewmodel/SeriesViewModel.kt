package com.example.embytv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embytv.data.EmbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SeriesUiState {
    object Loading : SeriesUiState
    data class Loaded(
        val seriesName: String,
        val seasons: List<com.example.embytv.data.model.MediaItem>,
        val overview: String?,
        val people: List<com.example.embytv.data.model.BasePerson>?
    ) : SeriesUiState
    data class Error(val message: String) : SeriesUiState
}

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val repository: EmbyRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SeriesUiState>(SeriesUiState.Loading)
    val state: StateFlow<SeriesUiState> = _state

    fun loadDetails(seriesId: String) {
        viewModelScope.launch {
            try {
                val details = repository.getItemDetails(seriesId)
                if (details != null) {
                    // Get all seasons for this series
                    val userId = repository.userId ?: ""
                    val itemsPage = repository.getUserLibrary(userId)
                    val seasons = itemsPage.items.filter { it.type == "Season" && it.parentId == seriesId }
                        .sortedBy { it.indexNumber ?: Int.MAX_VALUE }
                    
                    _state.value = SeriesUiState.Loaded(
                        seriesName = details.name,
                        seasons = seasons.ifEmpty { listOf(details) },
                        overview = details.overview,
                        people = details.people
                    )
                } else {
                    _state.value = SeriesUiState.Error("Failed to load series")
                }
            } catch (e: Exception) {
                _state.value = SeriesUiState.Error(e.message ?: "Network error")
            }
        }
    }
}
