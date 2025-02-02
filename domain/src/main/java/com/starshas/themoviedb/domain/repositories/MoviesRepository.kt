package com.starshas.themoviedb.domain.repositories

import com.starshas.themoviedb.domain.models.MovieResponse

interface MoviesRepository {
    suspend fun getNowPlayingMovies(apiKey: String): Result<MovieResponse>
}
