package com.starshas.themoviedb.domain.usecases

interface SetFavoriteUseCase {
    suspend operator fun invoke(
        movieId: Int,
        isFavorite: Boolean,
    )
}
