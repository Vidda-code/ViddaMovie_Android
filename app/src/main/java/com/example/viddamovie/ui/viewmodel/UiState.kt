package com.example.viddamovie.ui.viewmodel

import com.example.viddamovie.domain.model.Title

sealed class UiState<out T> {

    object Initial : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<T>(val data: T) : UiState<T>()

    data class Error(val exception: Throwable) : UiState<Nothing>()
}

data class HomeUiState(
    val trendingMovies: List<Title> = emptyList(),
    val trendingTV: List<Title> = emptyList(),
    val topRatedMovies: List<Title> = emptyList(),
    val topRatedTV: List<Title> = emptyList(),
    val heroTitle: Title? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class VideoUiState {
    object Loading : VideoUiState()
    data class Success(val videoId: String) : VideoUiState()
    data class Error(val message: String) : VideoUiState()
}