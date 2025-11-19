package com.example.viddamovie.util

import com.example.viddamovie.domain.model.Title
import kotlin.collections.map


object Constants {

    // POSTER & IMAGE URLS
    const val POSTER_URL_BASE = "https://image.tmdb.org/t/p/w500"

    const val TEST_POSTER_1 = "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg"
    const val TEST_POSTER_2 = "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg"
    const val TEST_POSTER_3 = "https://picsum.photos/200/300"

    // YOUTUBE API PARAMETERS
    object Youtube {
        const val TRAILER_KEYWORD = "trailer"
        const val QUERY_PARAM = "q"
        const val KEY_PARAM = "key"
        const val SPACE = " "
    }

    // MEDIA TYPES
    object MediaType {
        const val MOVIE = "movie"
        const val TV = "tv"
    }

    // API ENDPOINT TYPES
    object EndpointType {
        const val TRENDING = "trending"
        const val TOP_RATED = "top_rated"
        const val UPCOMING = "upcoming"
        const val SEARCH = "search"
    }

    // HELPER FUNCTIONS
    fun getFullPosterUrl(posterPath: String?): String? {
        return posterPath?.let {
            if (it.startsWith("http")) {
                // Already a full URL
                it
            } else {
                // Append to base URL
                POSTER_URL_BASE + it
            }
        }
    }

    fun buildTrailerSearchQuery(title: String): String {
        return "$title${Youtube.SPACE}${Youtube.TRAILER_KEYWORD}"
    }
}

fun List<Title>.withFullPosterUrls(): List<com.example.viddamovie.domain.model.Title> {
    return this.map { title ->
        title.copy(
            posterPath = Constants.getFullPosterUrl(title.posterPath)
        )
    }
}