package com.starshas.themoviedb.domain.usecases

import kotlinx.coroutines.flow.Flow

//Use case for retrieving the favorite status of a movie.
interface GetFavoriteStatusUseCase {
    operator fun invoke(movieId: Int): Flow<Boolean>
}
