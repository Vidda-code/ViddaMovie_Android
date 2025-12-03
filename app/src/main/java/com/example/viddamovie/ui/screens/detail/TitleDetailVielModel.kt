package com.example.viddamovie.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viddamovie.data.repository.ApiConfig
import com.example.viddamovie.domain.model.MediaType
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.domain.repository.TitleRepository
import com.example.viddamovie.ui.viewmodel.VideoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TitleDetailViewModel @Inject constructor(
    private val repository: TitleRepository,
    private val apiConfig: ApiConfig,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Title details state
     */
    private val _titleState = MutableStateFlow<TitleDetailState>(TitleDetailState.Loading)
    val titleState: StateFlow<TitleDetailState> = _titleState.asStateFlow()

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

    /**
     * Load title details from API
     */
    fun loadTitleDetails(titleId: Int, mediaType: MediaType = MediaType.MOVIE) {
        viewModelScope.launch {
            _titleState.value = TitleDetailState.Loading

            repository.getTitleDetails(titleId, mediaType).fold(
                onSuccess = { title ->
                    _titleState.value = TitleDetailState.Success(title)
                    // Auto-load video when title loads successfully
                    loadVideoId(title.displayTitle)
                },
                onFailure = { error ->
                    _titleState.value = TitleDetailState.Error(
                        error.message ?: "Failed to load title details"
                    )
                }
            )
        }
    }

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

sealed class TitleDetailState {
    object Loading : TitleDetailState()
    data class Success(val title: Title) : TitleDetailState()
    data class Error(val message: String) : TitleDetailState()
}

sealed class SaveState {
    object Idle : SaveState()
    object Saving : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}