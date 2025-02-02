package com.starshas.themoviedb.domain.usecases

import com.starshas.themoviedb.domain.repositories.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteStatusUseCaseImpl(private val favoritesRepository: FavoritesRepository):
    GetFavoriteStatusUseCase {
    override operator fun invoke(movieId: Int): Flow<Boolean> {
        return favoritesRepository.isFavorite(movieId)
    }
}
