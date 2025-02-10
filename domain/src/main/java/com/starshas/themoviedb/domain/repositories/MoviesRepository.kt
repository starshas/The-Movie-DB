package com.starshas.themoviedb.domain.repositories

import com.starshas.themoviedb.domain.models.DomainMoviesInfo

interface MoviesRepository {
    suspend fun getNowPlayingMovies(): Result<DomainMoviesInfo>
}
