package com.example.viddamovie.data.mapper

import com.example.viddamovie.data.local.TitleEntity
import com.example.viddamovie.data.remote.dto.TitleDto
import com.example.viddamovie.domain.model.MediaType
import com.example.viddamovie.domain.model.Title
import com.example.viddamovie.util.Constants

fun TitleDto.toDomain(explicitMediaType: MediaType? = null): Title? {
    // Must have valid ID to be useful
    val validId = id ?: return null
    val media = explicitMediaType ?: MediaType.fromString(mediaType)

    return Title(
        id = validId,
        title = title,
        name = name,
        overview = overview,
        // Transform poster path to full URL
        posterPath = posterPath?.let { Constants.getFullPosterUrl(it) },
        backdropPath = backdropPath?.let { Constants.getFullPosterUrl(it) },
        releaseDate = if (media == MediaType.TV) firstAirDate else releaseDate,
        voteAverage = voteAverage,
        mediaType = media
    )
}

fun List<TitleDto>.toDomainFromDto(explicitMediaType: MediaType? = null): List<Title> {
    return this.mapNotNull { it.toDomain(explicitMediaType) }
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