package com.starshas.themoviedb.domain.repositories

import com.starshas.themoviedb.domain.models.DomainMovieResponse

interface MoviesRepository {
    suspend fun getNowPlayingMovies(apiKey: String): Result<DomainMovieResponse>
}
