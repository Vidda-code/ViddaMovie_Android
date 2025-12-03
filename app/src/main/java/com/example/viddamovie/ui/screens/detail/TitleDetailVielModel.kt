package com.example.viddamovie.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viddamovie.data.repository.ApiConfig
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.domain.repository.TitleRepository
import com.example.viddamovie.ui.viewmodel.VideoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Title Detail screen
 *
 * iOS Equivalent: Part of ViewModel.swift (videoId related code)
 */
@HiltViewModel
class TitleDetailViewModel @Inject constructor(
    private val repository: TitleRepository,
    private val apiConfig: ApiConfig,  // Inject ApiConfig here!
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Video ID state for YouTube player
     */
    private val _videoState = MutableStateFlow<VideoUiState>(VideoUiState.Loading)
    val videoState: StateFlow<VideoUiState> = _videoState.asStateFlow()

    /**
     * Save result state
     */
    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    /**
     * API Config for YouTube player
     */
    val youtubeConfig: ApiConfig = apiConfig

    fun loadVideoId(titleName: String) {
        viewModelScope.launch {
            _videoState.value = VideoUiState.Loading

            repository.getTrailerVideoId(titleName).fold(
                onSuccess = { videoId ->
                    _videoState.value = VideoUiState.Success(videoId)
                },
                onFailure = { error ->
                    _videoState.value = VideoUiState.Error(
                        error.message ?: "Failed to load trailer"
                    )
                }
            )
        }
    }

    fun saveTitle(title: Title) {
        viewModelScope.launch {
            _saveState.value = SaveState.Saving

            repository.saveTitle(title).fold(
                onSuccess = {
                    _saveState.value = SaveState.Success
                },
                onFailure = { error ->
                    _saveState.value = SaveState.Error(
                        error.message ?: "Failed to save"
                    )
                }
            )
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }
}

sealed class SaveState {
    object Idle : SaveState()
    object Saving : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}