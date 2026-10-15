package com.example.embytv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embytv.data.EmbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: EmbyRepository
) : ViewModel() {

    var currentItemId: String = ""
        private set
    var currentMediaSourceId: String = ""
        private set

    fun initialize(itemId: String, mediaSourceId: String) {
        currentItemId = itemId
        currentMediaSourceId = mediaSourceId
    }

    fun startPlayback(positionTicks: Long = 0): Boolean {
        return repository.getPlaybackInfo(currentItemId, positionTicks) != null
    }
}
