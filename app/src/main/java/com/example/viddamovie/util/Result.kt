package com.example.viddamovie.util

sealed class Resource<out T> {

    object Loading : Resource<Nothing>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error(
        val exception: Throwable,
        val message: String? = exception.message
    ) : Resource<Nothing>()
}

inline fun <T> Resource<T>.handle(
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit = {},
    onError: (Throwable) -> Unit = {}
) {
    when (this) {
        is Resource.Loading -> onLoading()
        is Resource.Success -> onSuccess(data)
        is Resource.Error -> onError(exception)
    }
}

fun <T> Resource<T>.dataOrNull(): T? {
    return if (this is Resource.Success) data else null
}

fun <T> Resource<T>.isSuccess(): Boolean = this is Resource.Success

fun <T> Resource<T>.isLoading(): Boolean = this is Resource.Loading

fun <T> Resource<T>.isError(): Boolean = this is Resource.Error
