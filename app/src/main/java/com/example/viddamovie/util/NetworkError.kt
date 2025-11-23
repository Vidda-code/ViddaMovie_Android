package com.example.viddamovie.util

sealed class NetworkError(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    data class BadResponse(
        val statusCode: Int,
        val responseMessage: String? = null,
        override val cause: Throwable? = null
    ) : NetworkError(
        message = "HTTP Error $statusCode: ${responseMessage ?: "Unknown error"}",
        cause = cause
    ) {
        fun isClientError(): Boolean = statusCode in 400..499
        fun isServerError(): Boolean = statusCode in 500..599
    }

    object MissingConfig : NetworkError(
        message = "API configuration is missing. Restart the app and try again."
    ) {
        override fun toString(): String = message ?: "Missing API Configuration"
    }

    data class UrlBuildFailed(
        val attemptedUrl: String? = null,
        override val cause: Throwable? = null
    ) : NetworkError(
        message = "Failed to build URL${attemptedUrl?.let { ": $it" } ?: ""}",
        cause = cause
    )

    object NoConnection : NetworkError(
        message = "No internet connection. Please check your network settings."
    )


    data class Timeout(
        override val cause: Throwable? = null
    ) : NetworkError(
        message = "Request timed out. Please try again.",
        cause = cause
    )

    data class ParseError(
        val jsonString: String? = null,
        override val cause: Throwable? = null
    ) : NetworkError(
        message = "Failed to parse API response",
        cause = cause
    )

    data class Unknown(
        val errorMessage: String? = null,
        override val cause: Throwable? = null
    ) : NetworkError(
        message = errorMessage ?: "An unknown network error occurred",
        cause = cause
    )
}

val NetworkError.userMessage: String
    get() = when (this) {
        is NetworkError.BadResponse -> {
            when {
                isClientError() -> "Invalid request. Please try again."
                isServerError() -> "Server error. Please try again later."
                else -> "Network error occurred."
            }
        }

        is NetworkError.MissingConfig -> "App configuration error. Please reinstall the app."
        is NetworkError.UrlBuildFailed -> "Invalid URL. Please contact support."
        is NetworkError.NoConnection -> "No internet connection. Please check your network."
        is NetworkError.Timeout -> "Request timed out. Please try again."
        is NetworkError.ParseError -> "Error processing data. Please try again."
        is NetworkError.Unknown -> errorMessage ?: "Something went wrong. Please try again."
    }

val NetworkError.isRetryable: Boolean
    get() = when (this) {
        is NetworkError.BadResponse -> isServerError()
        is NetworkError.NoConnection -> true
        is NetworkError.Timeout -> true
        is NetworkError.MissingConfig -> false
        is NetworkError.UrlBuildFailed -> false
        is NetworkError.ParseError -> false
        is NetworkError.Unknown -> false
    }
