package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.domain.models.DomainMoviesInfo
import com.starshas.themoviedb.domain.repositories.MoviesRepository

class GetNowPlayingMoviesUseCaseImpl(
    private val repository: MoviesRepository,
) : GetNowPlayingMoviesUseCase {
    override suspend operator fun invoke(): Result<DomainMoviesInfo> = repository.getNowPlayingMovies()
}
