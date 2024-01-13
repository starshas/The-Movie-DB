package com.starshas.themoviedb.data.repositories

import com.starshas.themoviedb.data.models.MovieResponse

interface MoviesRepository {
    suspend fun getNowPlayingMovies(apiKey: String): Result<MovieResponse>
}
