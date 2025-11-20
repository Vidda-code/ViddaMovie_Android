package com.example.viddamovie

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Setting up hilt for dependency injection
@HiltAndroidApp
class ViddaMovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}