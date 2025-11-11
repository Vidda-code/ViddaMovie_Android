package com.example.viddamovie.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Upcoming : Screen("upcoming")
    object Search : Screen("search")
    object Download : Screen("download")

//    // If you need routes with arguments
//    object CurrencyDetail : Screen("currency_detail/{currencyCode}") {
//        fun createRoute(currencyCode: String) = "currency_detail/$currencyCode"
//    }
}