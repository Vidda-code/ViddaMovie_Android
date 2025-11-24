package com.example.viddamovie.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
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

    init {
        // Load trending movies initially (when search is empty)
        loadTrending()

        // Set up search query debouncing
        setupSearchDebouncing()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebouncing() {
        searchQuery
            .debounce(500)  // Wait 500ms after user stops typing
            .distinctUntilChanged()  // Only search if query actually changed
            .onEach { query ->
                if (query.isEmpty()) {
                    loadTrending()
                } else {
                    performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleMediaType() {
        _isSearchingMovies.value = !_isSearchingMovies.value

        // Re-search with new media type
        val currentQuery = _searchQuery.value
        if (currentQuery.isEmpty()) {
            loadTrending()
        } else {
            performSearch(currentQuery)
        }
    }

    private fun loadTrending() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = if (_isSearchingMovies.value) {
                repository.getTrendingMovies()
            } else {
                repository.getTrendingTV()
            }

            result.fold(
                onSuccess = { titles ->
                    _searchResults.value = titles
                    _isLoading.value = false
                },
                onFailure = { error ->
                    _errorMessage.value = error.message
                    _isLoading.value = false
                }
            )
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = if (_isSearchingMovies.value) {
                repository.searchMovies(query)
            } else {
                repository.searchTV(query)
            }

            result.fold(
                onSuccess = { titles ->
                    _searchResults.value = titles
                    _isLoading.value = false
                },
                onFailure = { error ->
                    _errorMessage.value = error.message
                    _searchResults.value = emptyList()
                    _isLoading.value = false
                }
            )
        }
    }
}