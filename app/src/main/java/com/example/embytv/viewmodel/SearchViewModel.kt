package com.example.embytv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embytv.data.EmbyRepository
import com.example.embytv.data.model.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchUiState {
    object Idle : SearchUiState
    object Loading : SearchUiState
    data class Success(val results: List<MediaItem>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: EmbyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    fun searchItems(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }
        
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                val userId = repository.userId ?: ""
                // Emby Search API requires Items endpoint with Name query parameter
                val page = repository.getUserLibrary(userId)
                // Filter locally since we don't have a dedicated search endpoint yet
                val results = page.items.filter { 
                    it.name.contains(query, ignoreCase = true) 
                }.take(30)
                
                _uiState.value = SearchUiState.Success(results)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.message ?: "Search failed")
            }
        }
    }
}
