package com.example.viddamovie.ui.screens.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.domain.repository.TitleRepository
import com.example.viddamovie.ui.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val repository: TitleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Title>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<Title>>> = _uiState.asStateFlow()

    init {
        loadUpcomingMovies()
    }

    fun loadUpcomingMovies() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            repository.getUpcomingMovies().fold(
                onSuccess = { titles ->
                    _uiState.value = UiState.Success(titles)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error)
                }
            )
        }
    }

    fun retry() {
        loadUpcomingMovies()
    }
}