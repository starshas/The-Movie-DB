package com.starshas.themoviedb.domain.usecases

interface GetNowPlayingMoviesUseCase {
    suspend operator fun invoke(apiKey: String): Result<com.starshas.themoviedb.domain.models.MovieResponse>
}
