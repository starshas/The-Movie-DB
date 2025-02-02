package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.domain.models.DomainMovieResponse
import com.starshas.themoviedb.domain.repositories.MoviesRepository


class GetNowPlayingMoviesUseCaseImpl(private val repository: MoviesRepository):
    GetNowPlayingMoviesUseCase {
    override suspend operator fun invoke(apiKey: String): Result<DomainMovieResponse> =
        repository.getNowPlayingMovies(apiKey)
}
