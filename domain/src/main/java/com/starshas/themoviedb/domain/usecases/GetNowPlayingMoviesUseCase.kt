package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.domain.models.DomainMoviesInfo

interface GetNowPlayingMoviesUseCase {
    suspend operator fun invoke(): Result<DomainMoviesInfo>
}
