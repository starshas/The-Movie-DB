package com.starshas.themoviedb.data.mapper

import com.starshas.themoviedb.data.constants.DataConstants
import com.starshas.themoviedb.data.models.DateRange
import com.starshas.themoviedb.data.models.Movie
import com.starshas.themoviedb.data.models.MovieResponse
import com.starshas.themoviedb.domain.models.DateRange as DomainDateRange
import com.starshas.themoviedb.domain.models.Movie as DomainMovie
import com.starshas.themoviedb.domain.models.MovieResponse as DomainMovieResponse

fun Movie.toDomainModel(): DomainMovie {
    return DomainMovie(
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
        voteCount = voteCount
    )
}

 fun DateRange.toDomainModel(): DomainDateRange {
    return DomainDateRange(
        maximum = maximum,
        minimum = minimum
    )
}

fun MovieResponse.toDomainModel(): DomainMovieResponse {
    return DomainMovieResponse(
        dates = dates.toDomainModel(),
        page = page,
        results = results.map { it.toDomainModel() },
        totalPages = totalPages,
        totalResults = totalResults
    )
}
