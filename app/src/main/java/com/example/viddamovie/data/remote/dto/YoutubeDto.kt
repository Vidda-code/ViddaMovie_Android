package com.example.viddamovie.data.remote.dto

import com.google.gson.annotations.SerializedName

data class YoutubeSearchResponse(
    @SerializedName("items")
    val items: List<YoutubeItem>? = null
)

data class YoutubeItem(
    @SerializedName("id")
    val id: YoutubeId? = null
)

data class YoutubeId(
    @SerializedName("videoId")
    val videoId: String? = null
)
