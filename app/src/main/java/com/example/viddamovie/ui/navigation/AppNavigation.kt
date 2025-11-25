package com.example.viddamovie.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.viddamovie.data.repository.ApiConfig
import com.example.viddamovie.data.repository.ApiConfigLoader
import com.example.viddamovie.ui.screens.detail.TitleDetailScreen
import com.example.viddamovie.ui.screens.download.DownloadScreen
import com.example.viddamovie.ui.screens.home.HomeScreen
import com.example.viddamovie.ui.screens.search.SearchScreen
import com.example.viddamovie.ui.screens.upcoming.UpcomingScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(Screen.Upcoming.route) {
                UpcomingScreen(navController = navController)
            }

            composable(Screen.Search.route) {
                SearchScreen(navController = navController)
            }

            composable(Screen.Download.route) {
                DownloadScreen(navController = navController)
            }

            // Detail screen with title ID argument
            composable(
                route = "detail/{titleId}",
                arguments = listOf(
                    navArgument("titleId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val titleId = backStackEntry.arguments?.getInt("titleId") ?: 0
                // TODO: Inject ApiConfig properly via Hilt
                // For now, we'll load it here (not ideal but works)
                TitleDetailScreen(
                    titleId = titleId,
                    navController = navController,
                    apiConfig = ApiConfig(
                        tmdbBaseURL = "",
                        tmdbApiKey = "",
                        youtubeBaseURL = "https://www.youtube.com/embed",
                        youtubeApiKey = "",
                        youtubeSearchURL = ""
                    )
                )
            }
        }
    }
}