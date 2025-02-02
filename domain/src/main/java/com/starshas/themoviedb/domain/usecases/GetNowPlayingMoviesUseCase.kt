package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.domain.models.DomainMovieResponse

interface GetNowPlayingMoviesUseCase {
    suspend operator fun invoke(apiKey: String): Result<DomainMovieResponse>
}
