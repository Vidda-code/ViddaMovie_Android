package com.example.viddamovie.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viddamovie.domain.repository.TitleRepository
import com.example.viddamovie.ui.viewmodel.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TitleRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTitles()
    }

    fun loadTitles() {
        // Don't reload if already loaded
        if (_uiState.value.trendingMovies.isNotEmpty()) return

        // viewModelScope: Coroutine scope tied to ViewModel lifecycle
        // Automatically cancels when ViewModel is destroyed
        viewModelScope.launch {
            // Set loading state
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Fetch all data concurrently (like async let in Swift)
                val trendingMoviesResult = repository.getTrendingMovies()
                val trendingTVResult = repository.getTrendingTV()
                val topRatedMoviesResult = repository.getTopRatedMovies()
                val topRatedTVResult = repository.getTopRatedTV()

                // Extract data or use empty list
                val trendingMovies = trendingMoviesResult.getOrElse { emptyList() }
                val trendingTV = trendingTVResult.getOrElse { emptyList() }
                val topRatedMovies = topRatedMoviesResult.getOrElse { emptyList() }
                val topRatedTV = topRatedTVResult.getOrElse { emptyList() }

                // Select random hero title
                val heroTitle = trendingMovies.randomOrNull() ?: trendingMovies.firstOrNull()

                // Update state with all data
                _uiState.update {
                    it.copy(
                        trendingMovies = trendingMovies,
                        trendingTV = trendingTV,
                        topRatedMovies = topRatedMovies,
                        topRatedTV = topRatedTV,
                        heroTitle = heroTitle,
                        isLoading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {
                // Handle any errors
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun retry() {
        loadTitles()
    }
}