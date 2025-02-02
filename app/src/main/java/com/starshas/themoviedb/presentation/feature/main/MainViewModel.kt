package com.starshas.themoviedb.presentation.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starshas.themoviedb.BuildConfig
import com.starshas.themoviedb.R
import com.starshas.themoviedb.domain.models.DomainApiError
import com.starshas.themoviedb.domain.models.DomainMoviesInfo
import com.starshas.themoviedb.domain.usecases.GetFavoriteStatusUseCase
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCase
import com.starshas.themoviedb.domain.usecases.SetFavoriteUseCase
import com.starshas.themoviedb.domain.utils.StringProvider
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
    private var _listMovies: MutableLiveData<List<DomainMoviesInfo.Movie>> = MutableLiveData()
    val listMovie: LiveData<List<DomainMoviesInfo.Movie>> = _listMovies
    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchNowPlayingMovies()
    }

    fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            val result: Result<DomainMoviesInfo> = useCaseGetNowPlayingMovies(BuildConfig.API_KEY)

            result.fold({ value: DomainMoviesInfo ->
                _listMovies.value = value.results
                resetErrorMessage()
            }, { throwable: Throwable ->
                val error = throwable as DomainApiError
                _errorMessage.value = when (error) {
                    is DomainApiError.GenericError ->
                        stringProvider.getString(R.string.main_error_while_loading_the_list)
                    is DomainApiError.HttpError -> stringProvider.getHttpErrorMessage(
                        R.string.main_http_error,
                        httpCode = error.code,
                        message = error.errorMessage
                    )
                    DomainApiError.NetworkError -> stringProvider.getString(R.string.main_network_error)
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
