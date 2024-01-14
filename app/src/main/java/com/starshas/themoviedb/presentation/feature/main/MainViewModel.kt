package com.starshas.themoviedb.presentation.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starshas.themoviedb.BuildConfig
import com.starshas.themoviedb.R
import com.starshas.themoviedb.data.ApiError
import com.starshas.themoviedb.data.models.Movie
import com.starshas.themoviedb.data.models.MovieResponse
import com.starshas.themoviedb.domain.usecases.GetFavoriteStatusUseCase
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCase
import com.starshas.themoviedb.domain.usecases.SetFavoriteUseCase
import com.starshas.themoviedb.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseGetNowPlayingMovies: GetNowPlayingMoviesUseCase,
    private val stringProvider: StringProvider,
    private val setFavoriteUseCase: SetFavoriteUseCase,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase
) : ViewModel() {
    private var _listMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    val listMovie: LiveData<List<Movie>> = _listMovies
    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchNowPlayingMovies()
    }

    fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            val result: Result<MovieResponse> = useCaseGetNowPlayingMovies(BuildConfig.API_KEY)

            result.fold({ value: MovieResponse ->
                _listMovies.value = value.results
                resetErrorMessage()
            }, { throwable: Throwable ->
                val error = throwable as ApiError
                _errorMessage.value = when (error) {
                    is ApiError.GenericError ->
                        stringProvider.getString(R.string.main_error_while_loading_the_list)
                    is ApiError.HttpError -> stringProvider.getHttpErrorMessage(
                        R.string.main_http_error,
                        httpCode = error.code,
                        message = error.errorMessage
                    )
                    ApiError.NetworkError -> stringProvider.getString(R.string.main_network_error)
                }
            })
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun setFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            setFavoriteUseCase(movieId, isFavorite)
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> =
        getFavoriteStatusUseCase(movieId)
}
