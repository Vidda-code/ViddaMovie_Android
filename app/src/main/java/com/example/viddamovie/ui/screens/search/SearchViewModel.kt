package com.example.viddamovie.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TitleRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Title>>(emptyList())
    val searchResults: StateFlow<List<Title>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSearchingMovies = MutableStateFlow(true)
    val isSearchingMovies: StateFlow<Boolean> = _isSearchingMovies.asStateFlow()

    private var searchJob: Job? = null

    init {
        // Load trending movies initially and set up search query debouncing
        setupSearchDebouncing()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebouncing() {
        searchQuery
            .debounce(500)  // Wait 500ms after user stops typing
            .distinctUntilChanged()  // Only search if query actually changed
            .onEach { executeSearch() }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleMediaType() {
        _isSearchingMovies.update { !it }
        // Re-search with new media type
        executeSearch()
    }

    private fun executeSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val query = searchQuery.value

            val result = if (query.isBlank()) {
                if (isSearchingMovies.value) {
                    repository.getTrendingMovies()
                } else {
                    repository.getTrendingTV()
                }
            } else {
                if (isSearchingMovies.value) {
                    repository.searchMovies(query)
                } else {
                    repository.searchTV(query)
                }
            }

            result.fold(
                onSuccess = { titles ->
                    _searchResults.value = titles
                },
                onFailure = { error ->
                    _errorMessage.value = error.message
                    _searchResults.value = emptyList()
                }
            )
            _isLoading.value = false
        }
    }
}
