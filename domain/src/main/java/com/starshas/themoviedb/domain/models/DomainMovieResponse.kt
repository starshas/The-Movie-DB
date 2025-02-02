package com.starshas.themoviedb.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Domain model representing the response containing movies.
data class DomainMovieResponse(
    val dates: DateRange,
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
) {
    @Parcelize
    data class Movie(
        val adult: Boolean,
        val backdropUrl: String,
        val genreIds: List<Int>,
        val id: Int,
        val originalLanguage: String,
        val originalTitle: String,
        val overview: String,
        val popularity: Double,
        val posterUrl: String,
        val releaseDate: String,
        val title: String,
        val video: Boolean,
        val voteAverage: Double,
        val voteCount: Int
    ) : Parcelable

    data class DateRange(
        val maximum: String,
        val minimum: String
    )
}
