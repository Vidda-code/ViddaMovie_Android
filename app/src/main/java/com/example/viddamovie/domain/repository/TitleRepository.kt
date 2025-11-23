package com.example.viddamovie.domain.repository

import com.example.viddamovie.domain.model.Title
import kotlinx.coroutines.flow.Flow

interface TitleRepository {

    suspend fun getTrendingMovies(): Result<List<Title>>

    suspend fun getTrendingTV(): Result<List<Title>>

    suspend fun getTopRatedMovies(): Result<List<Title>>

    suspend fun getTopRatedTV(): Result<List<Title>>

    suspend fun getUpcomingMovies(): Result<List<Title>>

    suspend fun searchMovies(query: String): Result<List<Title>>

    suspend fun searchTV(query: String): Result<List<Title>>

    suspend fun getTrailerVideoId(titleName: String): Result<String>

    suspend fun saveTitle(title: Title): Result<Unit>

    suspend fun deleteTitle(title: Title): Result<Unit>

    fun getSavedTitles(): Flow<List<Title>>
}