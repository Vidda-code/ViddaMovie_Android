package com.example.viddamovie.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Upcoming : Screen("upcoming")
    object Search : Screen("search")
    object Download : Screen("download")
}