package com.example.viddamovie.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.viddamovie.data.repository.ApiConfig
import com.example.viddamovie.domain.model.MediaType
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.ui.screens.components.ErrorMessage
import com.example.viddamovie.ui.screens.components.GhostButton
import com.example.viddamovie.ui.screens.components.LoadingIndicator
import com.example.viddamovie.ui.screens.components.YoutubePlayer
import com.example.viddamovie.ui.viewmodel.VideoUiState
import kotlinx.coroutines.launch

@Composable
fun TitleDetailScreen(
    titleId: Int,
    navController: NavController,
    mediaType: MediaType = MediaType.MOVIE,
    viewModel: TitleDetailViewModel = hiltViewModel()
) {
    val titleState by viewModel.titleState.collectAsState()
    val videoState by viewModel.videoState.collectAsState()
    val saveState by viewModel.saveState.collectAsState()

    // 1. Initialize Snackbar Host State and Coroutine Scope
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Load title details when screen launches
    LaunchedEffect(titleId) {
        viewModel.loadTitleDetails(titleId, mediaType)
    }

    // Show save success message via state (optional if you want to use this instead of manual click)
    LaunchedEffect(saveState) {
        if (saveState is SaveState.Success) {
            kotlinx.coroutines.delay(2000)
            viewModel.resetSaveState()
        }
    }

    // 2. Wrap everything in a Scaffold to hold the SnackbarHost
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        // You can add a topBar here if needed, or keep it empty if your design covers it
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply scaffold padding
        ) {
            when (val state = titleState) {
                is TitleDetailState.Loading -> {
                    LoadingIndicator()
                }

                is TitleDetailState.Success -> {
                    val title = state.title

                    // Common download handler to avoid repeating code
                    val onDownloadHandler = {
                        viewModel.saveTitle(title)
                        // 3. Show the Snackbar
                        scope.launch {
                            snackbarHostState.showSnackbar("Movie added to Downloads")
                        }
                        Unit // Return Unit to match () -> Unit signature
                    }

                    when (val vState = videoState) {
                        is VideoUiState.Loading -> {
                            TitleDetailContent(
                                title = title,
                                videoId = null,
                                apiConfig = viewModel.youtubeConfig,
                                onDownloadClick = onDownloadHandler,
                                isSaving = saveState is SaveState.Saving,
                                isLoadingVideo = true
                            )
                        }

                        is VideoUiState.Success -> {
                            TitleDetailContent(
                                title = title,
                                videoId = vState.videoId,
                                apiConfig = viewModel.youtubeConfig,
                                onDownloadClick = onDownloadHandler,
                                isSaving = saveState is SaveState.Saving,
                                isLoadingVideo = false
                            )
                        }

                        is VideoUiState.Error -> {
                            TitleDetailContent(
                                title = title,
                                videoId = null,
                                apiConfig = viewModel.youtubeConfig,
                                onDownloadClick = onDownloadHandler,
                                isSaving = saveState is SaveState.Saving,
                                isLoadingVideo = false,
                                videoError = vState.message
                            )
                        }
                    }
                }

                is TitleDetailState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ErrorMessage(message = state.message)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadTitleDetails(titleId, mediaType) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleDetailContent(
    title: Title,
    videoId: String?,
    apiConfig: ApiConfig,
    onDownloadClick: () -> Unit,
    isSaving: Boolean,
    isLoadingVideo: Boolean = false,
    videoError: String? = null
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // YouTube Player or placeholder
        item {
            when {
                videoId != null -> {
                    YoutubePlayer(
                        videoId = videoId,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                isLoadingVideo -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                videoError != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Trailer not available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Title and overview
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = title.displayTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                title.releaseDate?.let { date ->
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                title.voteAverage?.let { rating ->
                    Text(
                        text = "Rating: ${String.format("%.1f", rating)}/10",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = title.overview ?: "No overview available",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Download button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp)
                        )
                    } else {
                        GhostButton(
                            text = "Download",
                            onClick = onDownloadClick
                        )
                    }
                }
            }
        }

        // Bottom padding
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
