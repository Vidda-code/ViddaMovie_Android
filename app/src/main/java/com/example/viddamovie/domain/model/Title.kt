package com.example.viddamovie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Title(
    val id: Int,
    val title: String?,
    val name: String?,
    val overview: String?,
    val posterPath: String?,  // Full URL: "https://image.tmdb.org/t/p/w500/abc.jpg"
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val mediaType: MediaType = MediaType.MOVIE
) : Parcelable {

    val displayTitle: String
        get() = name ?: title ?: "Unknown Title"

    val displayDate: String?
        get() = releaseDate ?: "Unknown"


    val ratingPercentage: Int
        get() = ((voteAverage ?: 0.0) * 10).toInt()


    val hasPoster: Boolean
        get() = !posterPath.isNullOrEmpty()

    companion object {

        val preview = Title(
            id = 1,
            title = "Fast And Furious",
            name = null,
            overview = "A movie about Fast And Furious",
            posterPath = "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg",
            backdropPath = null,
            releaseDate = "2023-01-01",
            voteAverage = 7.5,
            mediaType = MediaType.MOVIE
        )

        val previewList = listOf(
            Title(
                id = 1,
                title = "Fast And Furious",
                name = "Fast And Furious",
                overview = "A movie about Fast And Furious",
                posterPath = "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg",
                backdropPath = null,
                releaseDate = "2023-01-01",
                voteAverage = 7.5,
                mediaType = MediaType.MOVIE
            ),
            Title(
                id = 2,
                title = "Pulp Fiction",
                name = "Pulp Fiction",
                overview = "A movie about Pulp Fiction",
                posterPath = "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg",
                backdropPath = null,
                releaseDate = "1994-10-14",
                voteAverage = 8.9,
                mediaType = MediaType.MOVIE
            ),
            Title(
                id = 3,
                title = "The Dark Knight",
                name = "The Dark Knight",
                overview = "A movie about the Dark Knight",
                posterPath = "https://picsum.photos/200/300",
                backdropPath = null,
                releaseDate = "2008-07-18",
                voteAverage = 9.0,
                mediaType = MediaType.MOVIE
            )
        )
    }
}

enum class MediaType(val value: String) {
    MOVIE("movie"),
    TV("tv");

    companion object {
        fun fromString(value: String?): MediaType {
            return when (value?.lowercase()) {
                "tv" -> TV
                else -> MOVIE
            }
        }
    }
}