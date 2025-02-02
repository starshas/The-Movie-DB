package com.starshas.themoviedb.presentation.feature.movieinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starshas.themoviedb.domain.usecases.GetFavoriteStatusUseCase
import com.starshas.themoviedb.domain.usecases.SetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieInfoViewModel @Inject constructor(
    private val setFavoriteUseCase: com.starshas.themoviedb.domain.usecases.SetFavoriteUseCase,
    private val getFavoriteStatusUseCase: com.starshas.themoviedb.domain.usecases.GetFavoriteStatusUseCase
) : ViewModel() {
    fun setFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            setFavoriteUseCase(movieId, isFavorite)
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> =
        getFavoriteStatusUseCase(movieId)
}
