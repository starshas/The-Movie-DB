package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.data.models.MovieResponse

interface GetNowPlayingMoviesUseCase {
    suspend operator fun invoke(apiKey: String): Result<MovieResponse>
}
