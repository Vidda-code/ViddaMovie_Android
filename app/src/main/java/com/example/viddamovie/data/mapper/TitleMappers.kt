package com.example.viddamovie.data.mapper

import com.example.viddamovie.data.local.TitleEntity
import com.example.viddamovie.data.remote.dto.TitleDto
import com.example.viddamovie.domain.model.MediaType
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.util.Constants

fun TitleDto.toDomain(): Title? {
    // Must have valid ID to be useful
    val validId = id ?: return null

    return Title(
        id = validId,
        title = title,
        name = name,
        overview = overview,
        // Transform poster path to full URL
        posterPath = posterPath?.let { Constants.getFullPosterUrl(it) },
        backdropPath = backdropPath?.let { Constants.getFullPosterUrl(it) },
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        mediaType = MediaType.fromString(mediaType)
    )
}

fun List<TitleDto>.toDomainFromDto(): List<Title> {
    return this.mapNotNull { it.toDomain() }
    // mapNotNull = map + filter out nulls
}

fun Title.toEntity(): TitleEntity {
    return TitleEntity(
        id = id,
        title = title,
        name = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        mediaType = mediaType.value,
        savedAt = System.currentTimeMillis()  // Track when saved
    )
}

fun TitleEntity.toDomain(): Title {
    return Title(
        id = id,
        title = title,
        name = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        mediaType = MediaType.fromString(mediaType)
    )
}

fun List<TitleEntity>.toDomainFromEntity(): List<Title> {
    return this.map { it.toDomain() }
}
