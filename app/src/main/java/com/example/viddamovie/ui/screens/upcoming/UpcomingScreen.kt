package com.example.viddamovie.ui.screens.upcoming

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.viddamovie.ui.screens.components.ErrorMessage
import com.example.viddamovie.ui.screens.components.LoadingIndicator
import com.example.viddamovie.ui.screens.components.VerticalList
import com.example.viddamovie.ui.viewmodel.UiState

@Composable
fun UpcomingScreen(
    navController: NavController,
    viewModel: UpcomingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is UiState.Initial -> {
                // Loading will show immediately from init
            }

            is UiState.Loading -> {
                LoadingIndicator()
            }

            is UiState.Success -> {
                val titles = (uiState as UiState.Success).data
                VerticalList(
                    titles = titles,
                    onTitleClick = { title ->
                        navController.navigate("detail/${title.id}")
                    },
                    onDeleteClick = null  // No delete in upcoming
                )
            }

            is UiState.Error -> {
                val error = (uiState as UiState.Error).exception
                ErrorMessage(
                    message = error.message ?: "Unknown error",
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}