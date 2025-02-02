package com.starshas.themoviedb.data.repositories

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun setFavorite(movieId: Int, isFavorite: Boolean)
    fun isFavorite(movieId: Int): Flow<Boolean>
}
