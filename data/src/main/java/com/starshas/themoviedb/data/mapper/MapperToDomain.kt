package com.starshas.themoviedb.data.mapper

import com.starshas.themoviedb.data.constants.DataConstants
import com.starshas.themoviedb.data.models.ApiError
import com.starshas.themoviedb.data.models.DataMovieResponse
import com.starshas.themoviedb.domain.models.DomainApiError
import com.starshas.themoviedb.domain.models.DomainMoviesInfo as DomainMovieResponse

fun DataMovieResponse.Movie.toDomainModel(): DomainMovieResponse.Movie =
    DomainMovieResponse.Movie(
        adult = adult,
        backdropUrl = "${DataConstants.BASE_URL_IMAGES}$backdropPath",
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterUrl = "${DataConstants.BASE_URL_IMAGES}$posterPath",
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
    )

fun DataMovieResponse.DatesRange.toDomainModel(): DomainMovieResponse.DateRange =
    DomainMovieResponse.DateRange(
        maximum = maximum,
        minimum = minimum,
    )

fun DataMovieResponse.toDomainModel(): DomainMovieResponse =
    DomainMovieResponse(
        dates = dates.toDomainModel(),
        page = page,
        results = results.map { it.toDomainModel() },
        totalPages = totalPages,
        totalResults = totalResults,
    )

fun ApiError.toDomainApiError(): DomainApiError =
    when (this) {
        is ApiError.NetworkError -> DomainApiError.NetworkError
        is ApiError.HttpError -> DomainApiError.HttpError(this.code, this.errorMessage)
        is ApiError.GenericError -> DomainApiError.GenericError(this.error)
    }
