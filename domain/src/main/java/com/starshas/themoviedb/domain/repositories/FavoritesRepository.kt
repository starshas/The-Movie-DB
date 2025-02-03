package com.starshas.themoviedb.domain.repositories

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun setFavorite(
        movieId: Int,
        isFavorite: Boolean,
    )

    fun isFavorite(movieId: Int): Flow<Boolean>
}
