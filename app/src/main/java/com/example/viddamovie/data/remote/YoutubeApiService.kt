package com.example.viddamovie.data.remote

import com.example.viddamovie.data.remote.dto.YoutubeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET("search")
    suspend fun searchVideo(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 5
    ): YoutubeSearchResponse
}