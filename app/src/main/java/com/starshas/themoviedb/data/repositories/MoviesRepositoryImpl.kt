package com.starshas.themoviedb.data.repositories

import com.starshas.themoviedb.data.ApiError
import com.starshas.themoviedb.data.MovieDbApi
import com.starshas.themoviedb.data.models.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MoviesRepositoryImpl(
    private val movieDbApi: MovieDbApi,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : MoviesRepository {
    override suspend fun getNowPlayingMovies(apiKey: String): Result<MovieResponse> =
        withContext(coroutineContext) {
            try {
                val response = movieDbApi.getNowPlayingMovies(apiKey)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Result.success(body)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(ApiError.HttpError(response.code(), errorMessage))
                }
            } catch (e: IOException) {
                Timber.e(e, "Got IOException while fetching now playing movies")
                Result.failure(ApiError.NetworkError)
            } catch (e: Exception) {
                Timber.e(e, "Got Exception while fetching now playing movies")
                Result.failure(ApiError.GenericError(e))
            }
        }
}
