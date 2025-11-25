package com.example.viddamovie.ui.screens.download

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.viddamovie.ui.screens.components.EmptyState
import com.example.viddamovie.ui.screens.components.VerticalList

@Composable
fun DownloadScreen(
    navController: NavController,
    viewModel: DownloadViewModel = hiltViewModel()
) {
    val savedTitles by viewModel.savedTitles.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (savedTitles.isEmpty()) {
            EmptyState(message = "No Downloads")
        } else {
            VerticalList(
                titles = savedTitles,
                onTitleClick = { title ->
                    navController.navigate("detail/${title.id}")
                },
                onDeleteClick = { title ->
                    viewModel.deleteTitle(title)
                }
            )
        }
    }
}