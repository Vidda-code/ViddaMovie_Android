package com.example.viddamovie.ui.navigation

import com.example.viddamovie.domain.model.MediaType

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Upcoming : Screen("upcoming")
    object Search : Screen("search")
    object Download : Screen("download")

    object Detail : Screen("detail/{titleId}/{mediaType}") {
        fun createRoute(titleId: Int, mediaType: MediaType) = "detail/$titleId/${mediaType.name}"
    }
}