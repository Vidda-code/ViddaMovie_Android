package com.example.viddamovie.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                LoadingIndicator()
            }

            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error ?: "Unknown error",
                    onRetry = { viewModel.retry() }
                )
            }

            else -> {
                HomeContent(
                    uiState = uiState,
                    onTitleClick = { title ->
                        // Navigate to detail screen
                        navController.navigate("detail/${title.id}")
                    },
                    onDownloadClick = { title ->
                        // TODO: Save to downloads
                    }
                )
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
                    onDownloadClick = { onDownloadClick(hero) }
                )
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
                header = "Trending TV",
                titles = uiState.trendingTV,
                onTitleClick = onTitleClick
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
                header = "Top Rated TV",
                titles = uiState.topRatedTV,
                onTitleClick = onTitleClick
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
        // Hero poster image
        AsyncImage(
            model = title.posterPath,
            contentDescription = title.displayTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay (like iOS LinearGradient)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 400f
                    )
                )
        )

        // Action buttons at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GhostButton(
                text = "Play",
                onClick = onPlayClick
            )

            GhostButton(
                text = "Download",
                onClick = onDownloadClick
            )
        }
    }
}