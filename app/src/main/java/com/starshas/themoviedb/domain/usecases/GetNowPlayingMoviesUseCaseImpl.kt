package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.data.models.MovieResponse
import com.starshas.themoviedb.data.repositories.MoviesRepository

class GetNowPlayingMoviesUseCaseImpl(private val repository: MoviesRepository): GetNowPlayingMoviesUseCase {
    override suspend operator fun invoke(apiKey: String): Result<MovieResponse> =
        repository.getNowPlayingMovies(apiKey)
}
