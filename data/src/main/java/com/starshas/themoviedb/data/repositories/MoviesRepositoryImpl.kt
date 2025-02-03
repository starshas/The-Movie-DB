package com.starshas.themoviedb.data.repositories

import com.starshas.themoviedb.data.MovieDbApi
import com.starshas.themoviedb.data.mapper.toDomainApiError
import com.starshas.themoviedb.data.mapper.toDomainModel
import com.starshas.themoviedb.data.models.ApiError
import com.starshas.themoviedb.domain.models.DomainMoviesInfo
import com.starshas.themoviedb.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MoviesRepositoryImpl(
    private val movieDbApi: MovieDbApi,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) : MoviesRepository {
    @Suppress("TooGenericExceptionCaught")
    override suspend fun getNowPlayingMovies(apiKey: String): Result<DomainMoviesInfo> =
        withContext(coroutineContext) {
            try {
                val response = movieDbApi.getNowPlayingMovies(apiKey)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Result.success(body.toDomainModel())
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(ApiError.HttpError(response.code(), errorMessage).toDomainApiError())
                }
            } catch (e: IOException) {
                Timber.e(e, "IOException while fetching now playing movies")
                Result.failure(ApiError.NetworkError.toDomainApiError())
            } catch (e: Exception) {
                Timber.e(e, "Exception while fetching now playing movies")
                Result.failure(ApiError.GenericError(e).toDomainApiError())
            }
        }
}
