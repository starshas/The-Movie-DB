package com.starshas.themoviedb.data

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun setFavoriteMovie(
        movieId: Int,
        isFavorite: Boolean,
    )

    fun isFavoriteMoviesFlow(movieId: Int): Flow<Boolean>
}
