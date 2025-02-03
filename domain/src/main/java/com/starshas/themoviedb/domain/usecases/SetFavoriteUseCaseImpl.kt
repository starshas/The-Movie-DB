package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.domain.repositories.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SetFavoriteUseCaseImpl(
    private val favoritesRepository: FavoritesRepository,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) : SetFavoriteUseCase {
    override suspend operator fun invoke(
        movieId: Int,
        isFavorite: Boolean,
    ) = withContext(coroutineContext) {
        favoritesRepository.setFavorite(movieId, isFavorite)
    }
}
