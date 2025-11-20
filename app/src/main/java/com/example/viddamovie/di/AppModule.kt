package com.example.viddamovie.di

import android.content.Context
import com.example.viddamovie.data.repository.ApiConfig
import com.example.viddamovie.data.repository.ApiConfigLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiConfig(
        @ApplicationContext context: Context
    ): ApiConfig {
        // Load config from assets once
        return ApiConfigLoader.loadConfig(context)
    }

    @Provides
    @Singleton
    fun provideTmdbBaseUrl(apiConfig: ApiConfig): String {
        return apiConfig.tmdbBaseURL
    }

    @Provides
    @Singleton
    fun provideTmdbApiKey(apiConfig: ApiConfig): String {
        return apiConfig.tmdbApiKey
    }

    @Provides
    @Singleton
    fun provideYoutubeBaseUrl(apiConfig: ApiConfig): String {
        return apiConfig.youtubeBaseURL
    }

    @Provides
    @Singleton
    fun provideYoutubeApiKey(apiConfig: ApiConfig): String {
        return apiConfig.youtubeApiKey
    }

    @Provides
    @Singleton
    fun provideYoutubeSearchUrl(apiConfig: ApiConfig): String {
        return apiConfig.youtubeSearchURL
    }
}