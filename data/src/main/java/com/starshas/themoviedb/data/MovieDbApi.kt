package com.starshas.themoviedb.data

import com.starshas.themoviedb.data.models.DataMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
    ): Response<DataMovieResponse>
}
