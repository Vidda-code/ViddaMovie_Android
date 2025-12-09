package com.example.viddamovie.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.viddamovie.ui.screens.components.ErrorMessage
import com.example.viddamovie.ui.screens.components.GhostButton
import com.example.viddamovie.ui.screens.components.HorizontalList
import com.example.viddamovie.ui.screens.components.LoadingIndicator
import com.example.viddamovie.ui.viewmodel.HomeUiState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                LoadingIndicator()
            }

            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error ?: "Unknown error", onRetry = { viewModel.retry() })
            }

            else -> {
                HomeContent(uiState = uiState, onTitleClick = { title ->
                    // Navigate with media type
                    navController.navigate("detail/${title.id}/${title.mediaType}")
                }, onDownloadClick = { title ->
                    viewModel.saveTitle(title)
                    scope.launch {
                        snackbarHostState.showSnackbar("Movie added to Downloads")
                    }
                })
            }
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onTitleClick: (com.example.viddamovie.domain.model.Title) -> Unit,
    onDownloadClick: (com.example.viddamovie.domain.model.Title) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Hero Image Section
        item {
            uiState.heroTitle?.let { hero ->
                HeroSection(
                    title = hero,
                    onPlayClick = { onTitleClick(hero) },
                    onDownloadClick = { onDownloadClick(hero) })
            }
        }

        // Trending Movies
        item {
            HorizontalList(
                header = "Trending Movies",
                titles = uiState.trendingMovies,
                onTitleClick = onTitleClick
            )
        }

        // Trending TV
        item {
            HorizontalList(
                header = "Trending TV", titles = uiState.trendingTV, onTitleClick = onTitleClick
            )
        }

        // Top Rated Movies
        item {
            HorizontalList(
                header = "Top Rated Movies",
                titles = uiState.topRatedMovies,
                onTitleClick = onTitleClick
            )
        }

        // Top Rated TV
        item {
            HorizontalList(
                header = "Top Rated TV", titles = uiState.topRatedTV, onTitleClick = onTitleClick
            )
        }

        // Bottom padding
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun HeroSection(
    title: com.example.viddamovie.domain.model.Title,
    onPlayClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        AsyncImage(
            model = title.posterPath,
            contentDescription = title.displayTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, Color.Black.copy(alpha = 0.8f)
                        ), startY = 400f
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GhostButton(
                text = "Play", onClick = onPlayClick
            )

            GhostButton(
                text = "Download", onClick = onDownloadClick
            )
        }
    }
}