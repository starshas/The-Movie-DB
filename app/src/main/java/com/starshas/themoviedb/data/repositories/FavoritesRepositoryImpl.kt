package com.starshas.themoviedb.data.repositories

import com.starshas.themoviedb.data.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FavoritesRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : FavoritesRepository {
    override suspend fun setFavorite(movieId: Int, isFavorite: Boolean) =
        withContext(coroutineContext) {
            dataStoreManager.setFavoriteMovie(movieId, isFavorite)
        }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return dataStoreManager.isFavoriteMoviesFlow(movieId)
    }
}
