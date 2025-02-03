package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.domain.models.DomainMoviesInfo
import com.starshas.themoviedb.domain.repositories.MoviesRepository

class GetNowPlayingMoviesUseCaseImpl(
    private val repository: MoviesRepository,
) : GetNowPlayingMoviesUseCase {
    @Suppress("ktlint:standard:function-signature")
    override suspend operator fun invoke(
        apiKey: String,
    ): Result<DomainMoviesInfo> = repository.getNowPlayingMovies(apiKey)
}
