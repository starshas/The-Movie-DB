package com.starshas.themoviedb.domain.models

// Domain model representing the response containing movies.
data class MovieResponse(
    val dates: DateRange,
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)
