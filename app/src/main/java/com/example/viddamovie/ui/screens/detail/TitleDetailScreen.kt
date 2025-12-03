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
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.ui.screens.components.ErrorMessage
import com.example.viddamovie.ui.screens.components.GhostButton
import com.example.viddamovie.ui.screens.components.LoadingIndicator
import com.example.viddamovie.ui.screens.components.YoutubePlayer
import com.example.viddamovie.ui.viewmodel.VideoUiState

/**
 * Title Detail Screen with YouTube player
 *
 * iOS Equivalent: TitleDetailView.swift
 * ```swift
 * struct TitleDetailView: View {
 *     @Environment(\.dismiss) var dismiss
 *     let title: Title
 *     let viewModel = ViewModel()
 *     @Environment(\.modelContext) var modelContext
 *
 *     var body: some View {
 *         GeometryReader { geometry in
 *             switch viewModel.videoIdStatus {
 *             case .notStarted:
 *                 EmptyView()
 *             case .fetching:
 *                 ProgressView()
 *             case .success:
 *                 ScrollView {
 *                     LazyVStack(alignment: .leading) {
 *                         YoutubePlayer(videoId: viewModel.videoId)
 *                         Text(titleName).bold()
 *                         Text(title.overview ?? "")
 *                         Button { // Download }
 *                     }
 *                 }
 *             case .failed(let error):
 *                 Text(error.localizedDescription)
 *             }
 *         }
 *         .task {
 *             await viewModel.getVideoId(for: titleName)
 *         }
 *     }
 * }
 * ```
 */
@Composable
fun TitleDetailScreen(
    titleId: Int,
    navController: NavController,
    viewModel: TitleDetailViewModel = hiltViewModel()
) {
    val videoState by viewModel.videoState.collectAsState()
    val saveState by viewModel.saveState.collectAsState()

    // TODO: Load title details from repository using titleId
    // For now, using a placeholder
    val title = remember { Title.preview }

    LaunchedEffect(title) {
        viewModel.loadVideoId(title.displayTitle)
    }

    // Show save success message
    LaunchedEffect(saveState) {
        if (saveState is SaveState.Success) {
            // Could show a Snackbar here
            kotlinx.coroutines.delay(2000)
            viewModel.resetSaveState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (videoState) {
            is VideoUiState.Loading -> {
                LoadingIndicator()
            }

            is VideoUiState.Success -> {
                val videoId = (videoState as VideoUiState.Success).videoId
                TitleDetailContent(
                    title = title,
                    videoId = videoId,
                    apiConfig = viewModel.youtubeConfig,  // Get from ViewModel!
                    onDownloadClick = {
                        viewModel.saveTitle(title)
                    },
                    isSaving = saveState is SaveState.Saving
                )
            }

            is VideoUiState.Error -> {
                val message = (videoState as VideoUiState.Error).message
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ErrorMessage(message = message)
                    Spacer(modifier = Modifier.height(16.dp))
                    TitleDetailContentWithoutVideo(
                        title = title,
                        onDownloadClick = {
                            viewModel.saveTitle(title)
                        },
                        isSaving = saveState is SaveState.Saving
                    )
                }
            }
        }
    }
}

/**
 * Detail content with YouTube player
 */
@Composable
private fun TitleDetailContent(
    title: Title,
    videoId: String,
    apiConfig: ApiConfig,
    onDownloadClick: () -> Unit,
    isSaving: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // YouTube Player
        item {
            YoutubePlayer(
                videoId = videoId,
                apiConfig = apiConfig,
                modifier = Modifier.fillMaxWidth()
            )
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

/**
 * Detail content without video (fallback when video fails to load)
 */
@Composable
private fun TitleDetailContentWithoutVideo(
    title: Title,
    onDownloadClick: () -> Unit,
    isSaving: Boolean
) {
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

        Text(
            text = title.overview ?: "No overview available",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isSaving) {
            CircularProgressIndicator()
        } else {
            GhostButton(
                text = "Download",
                onClick = onDownloadClick
            )
        }
    }
}


/**
 * ============================================
 * DETAIL SCREEN COMPARISON
 * ============================================
 *
 * Video Player Integration:
 * -------------------------
 *
 * iOS:
 * ```swift
 * YoutubePlayer(videoId: viewModel.videoId)
 *     .aspectRatio(1.3, contentMode: .fit)
 * ```
 *
 * Android:
 * ```kotlin
 * YoutubePlayer(
 *     videoId = videoId,
 *     apiConfig = apiConfig,
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .height(220.dp)
 * )
 * ```
 *
 *
 * Save to Database:
 * -----------------
 *
 * iOS:
 * ```swift
 * Button {
 *     let saveTitle = title
 *     saveTitle.title = titleName
 *     modelContext.insert(saveTitle)
 *     try? modelContext.save()
 *     dismiss()
 * }
 * ```
 *
 * Android:
 * ```kotlin
 * GhostButton(
 *     text = "Download",
 *     onClick = {
 *         viewModel.saveTitle(title)
 *     }
 * )
 *
 * // In ViewModel:
 * fun saveTitle(title: Title) {
 *     viewModelScope.launch {
 *         repository.saveTitle(title)
 *     }
 * }
 * ```
 *
 *
 * Loading Video ID:
 * -----------------
 *
 * iOS:
 * ```swift
 * .task {
 *     await viewModel.getVideoId(for: titleName)
 * }
 * ```
 *
 * Android:
 * ```kotlin
 * LaunchedEffect(title) {
 *     viewModel.loadVideoId(title.displayTitle)
 * }
 * ```
 *
 * LaunchedEffect = Runs side effects in composition
 * Similar to .task in SwiftUI
 */