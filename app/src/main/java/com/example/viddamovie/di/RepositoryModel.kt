package com.example.viddamovie.di

import com.example.viddamovie.data.local.TitleDao
import com.example.viddamovie.data.remote.TmdbApiService
import com.example.viddamovie.data.remote.YoutubeApiService
import com.example.viddamovie.data.repository.ApiConfig
import com.example.viddamovie.data.repository.TitleRepositoryImpl
import com.example.viddamovie.domain.repository.TitleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTitleRepository(
        tmdbApi: TmdbApiService,
        youtubeApi: YoutubeApiService,
        titleDao: TitleDao,
        apiConfig: ApiConfig
    ): TitleRepository {
        return TitleRepositoryImpl(
            tmdbApi = tmdbApi,
            youtubeApi = youtubeApi,
            titleDao = titleDao,
            apiConfig = apiConfig
        )
    }
}