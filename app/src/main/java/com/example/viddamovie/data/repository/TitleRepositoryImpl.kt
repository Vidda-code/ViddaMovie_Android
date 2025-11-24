package com.example.viddamovie.data.repository

import com.example.viddamovie.data.local.TitleDao
import com.example.viddamovie.data.mapper.toDomain
import com.example.viddamovie.data.mapper.toDomainFromDto
import com.example.viddamovie.data.mapper.toDomainFromEntity
import com.example.viddamovie.data.mapper.toEntity
import com.example.viddamovie.data.remote.TmdbApiService
import com.example.viddamovie.data.remote.YoutubeApiService
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.domain.repository.TitleRepository
import com.example.viddamovie.util.Constants
import com.example.viddamovie.util.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of TitleRepository
 *
 * iOS Equivalent: Your DataFetcher + ViewModel combined
 *
 * Purpose: Actual implementation of data operations
 * - Makes API calls via Retrofit services
 * - Converts DTOs to domain models
 * - Handles errors
 * - Manages database operations
 *
 * @Inject constructor: Hilt provides all dependencies automatically!
 */
class TitleRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApiService,
    private val youtubeApi: YoutubeApiService,
    private val titleDao: TitleDao,
    private val apiConfig: ApiConfig
) : TitleRepository {

    /**
     * Get trending movies from TMDB API
     *
     * iOS Equivalent:
     * ```swift
     * trendingMovies = try await dataFetcher.fetchTitles(
     *     for: "movie",
     *     by: "trending"
     * )
     * ```
     */
    override suspend fun getTrendingMovies(): Result<List<Title>> {
        return safeApiCall {
            val response = tmdbApi.getTrending(
                media = "movie",
                apiKey = apiConfig.tmdbApiKey
            )
            response.results.toDomainFromDto()  // Convert DTO → Domain
        }
    }

    /**
     * Get trending TV shows
     */
    override suspend fun getTrendingTV(): Result<List<Title>> {
        return safeApiCall {
            val response = tmdbApi.getTrending(
                media = "tv",
                apiKey = apiConfig.tmdbApiKey
            )
            response.results.toDomainFromDto()
        }
    }

    /**
     * Get top rated movies
     */
    override suspend fun getTopRatedMovies(): Result<List<Title>> {
        return safeApiCall {
            val response = tmdbApi.getTopRated(
                media = "movie",
                apiKey = apiConfig.tmdbApiKey
            )
            response.results.toDomainFromDto()
        }
    }

    /**
     * Get top rated TV shows
     */
    override suspend fun getTopRatedTV(): Result<List<Title>> {
        return safeApiCall {
            val response = tmdbApi.getTopRated(
                media = "tv",
                apiKey = apiConfig.tmdbApiKey
            )
            response.results.toDomainFromDto()
        }
    }

    /**
     * Get upcoming movies
     */
    override suspend fun getUpcomingMovies(): Result<List<Title>> {
        return safeApiCall {
            val response = tmdbApi.getUpcoming(
                apiKey = apiConfig.tmdbApiKey
            )
            response.results.toDomainFromDto()
        }
    }

    override suspend fun searchMovies(query: String): Result<List<Title>> {
        return safeApiCall {
            val response = tmdbApi.search(
                media = "movie",
                apiKey = apiConfig.tmdbApiKey,
                query = query
            )
            response.results.toDomainFromDto()
        }
    }

    override suspend fun searchTV(query: String): Result<List<Title>> {
        return safeApiCall {
            val response = tmdbApi.search(
                media = "tv",
                apiKey = apiConfig.tmdbApiKey,
                query = query
            )
            response.results.toDomainFromDto()
        }
    }

    override suspend fun getTrailerVideoId(titleName: String): Result<String> {
        return safeApiCall {
            val searchQuery = Constants.buildTrailerSearchQuery(titleName)
            val response = youtubeApi.searchVideo(
                query = searchQuery,
                apiKey = apiConfig.youtubeApiKey
            )
            response.items?.firstOrNull()?.id?.videoId
                ?: throw NetworkError.ParseError(
                    jsonString = "No video ID found for $titleName"
                )
        }
    }

    override suspend fun saveTitle(title: Title): Result<Unit> {
        return try {
            val entity = title.toEntity()  // Convert Domain → Entity
            titleDao.insertTitle(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTitle(title: Title): Result<Unit> {
        return try {
            val entity = title.toEntity()
            titleDao.deleteTitle(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSavedTitles(): Flow<List<Title>> {
        return titleDao.getAllTitles()
            .map { entities -> entities.toDomainFromEntity() }  // Convert Entity → Domain
    }

    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: Exception) {
            Result.failure(
                when (e) {
                    is java.net.UnknownHostException -> NetworkError.NoConnection
                    is java.net.SocketTimeoutException -> NetworkError.Timeout()
                    is retrofit2.HttpException -> NetworkError.BadResponse(
                        statusCode = e.code(),
                        responseMessage = e.message()
                    )
                    else -> NetworkError.Unknown(e.message)
                }
            )
        }
    }
}