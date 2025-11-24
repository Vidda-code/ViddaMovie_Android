package com.example.viddamovie.ui.screens.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val repository: TitleRepository
) : ViewModel() {

    val savedTitles: StateFlow<List<Title>> = repository.getSavedTitles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteTitle(title: Title) {
        viewModelScope.launch {
            repository.deleteTitle(title)
            // No need to manually update UI!
            // savedTitles Flow automatically emits new list
        }
    }
}