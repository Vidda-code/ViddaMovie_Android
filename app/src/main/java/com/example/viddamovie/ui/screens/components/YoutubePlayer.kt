package com.example.viddamovie.ui.screens.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.viddamovie.data.repository.ApiConfig

@Composable
fun YoutubePlayer(
    videoId: String,
    apiConfig: ApiConfig,
    modifier: Modifier = Modifier
) {
    // Build YouTube embed URL
    val embedUrl = "${apiConfig.youtubeBaseURL}/$videoId"

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // Enable JavaScript (required for YouTube player)
                settings.javaScriptEnabled = true

                // Allow media playback without user gesture
                settings.mediaPlaybackRequiresUserGesture = false

                // Set WebView client
                webViewClient = WebViewClient()
            }
        },
        update = { webView ->
            // Load YouTube video
            webView.loadUrl(embedUrl)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}