package com.example.viddamovie.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class ApiConfig(
    @SerializedName("tmdbBaseURL")
    val tmdbBaseURL: String,

    @SerializedName("tmdbAPIKey")
    val tmdbApiKey: String,

    @SerializedName("youtubeBaseURL")
    val youtubeBaseURL: String,

    @SerializedName("youtubeAPIKey")
    val youtubeApiKey: String,

    @SerializedName("youtubeSearchURL")
    val youtubeSearchURL: String
)

object ApiConfigLoader {

    fun loadConfig(context: Context): ApiConfig {
        return try {

            val inputStream = context.assets.open("APIConfig.json")

            val json = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            gson.fromJson(json, ApiConfig::class.java)

        } catch (e: Exception) {
            when (e) {
                is java.io.FileNotFoundException -> {
                    throw ApiConfigException.FileNotFound(e)
                }
                is com.google.gson.JsonSyntaxException -> {
                    throw ApiConfigException.DecodingFailed(e)
                }
                else -> {
                    throw ApiConfigException.DataLoadingFailed(e)
                }
            }
        }
    }
}

sealed class ApiConfigException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    class FileNotFound(cause: Throwable) : ApiConfigException(
        message = "APIConfig.json file not found in assets folder",
        cause = cause
    )

    class DataLoadingFailed(cause: Throwable) : ApiConfigException(
        message = "Failed to load APIConfig.json: ${cause.message}",
        cause = cause
    )

    class DecodingFailed(cause: Throwable) : ApiConfigException(
        message = "Failed to decode APIConfig.json: ${cause.message}",
        cause = cause
    )
}
