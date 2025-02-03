package com.starshas.themoviedb.domain.usecases

import kotlinx.coroutines.flow.Flow

interface GetFavoriteStatusUseCase {
    operator fun invoke(movieId: Int): Flow<Boolean>
}
