package com.example.viddamovie.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        }
    }
}

