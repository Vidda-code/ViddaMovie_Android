package com.example.viddamovie.data.remote

import com.example.viddamovie.data.remote.dto.TmdbResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("3/trending/{media}/day")
    suspend fun getTrending(
        @Path("media") media: String,
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("3/{media}/top_rated")
    suspend fun getTopRated(
        @Path("media") media: String,
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("3/movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("3/search/{media}")
    suspend fun search(
        @Path("media") media: String,
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): TmdbResponse
}
