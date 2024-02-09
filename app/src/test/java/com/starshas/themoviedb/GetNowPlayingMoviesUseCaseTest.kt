package com.starshas.themoviedb

import com.starshas.themoviedb.data.ApiError
import com.starshas.themoviedb.data.MovieDbApi
import com.starshas.themoviedb.data.models.DateRange
import com.starshas.themoviedb.data.models.Movie
import com.starshas.themoviedb.data.models.MovieResponse
import com.starshas.themoviedb.data.repositories.MoviesRepository
import com.starshas.themoviedb.data.repositories.MoviesRepositoryImpl
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCase
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Response

@ExperimentalCoroutinesApi
class GetNowPlayingMoviesUseCaseTest {
    private lateinit var movieDbApi: MovieDbApi
    private lateinit var moviesRepository: MoviesRepository
    private lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
    private val movieInstanceExample = Movie(
        adult = false,
        backdropPath = "/pathToBackdrop.jpg",
        genreIds = listOf(28, 12, 16),
        id = 550,
        originalLanguage = "en",
        originalTitle = "Fight Club",
        overview = "Overview",
        popularity = 0.5,
        posterPath = "/pathToPoster.jpg",
        releaseDate = "1999-10-15",
        title = "Fight Club",
        video = false,
        voteAverage = 8.8,
        voteCount = 12345
    )
    private val dateRangeExample = DateRange(
        maximum = "2023-12-20T23:59:59-05:00",
        minimum = "2023-01-15T00:00:00-05:00"
    )

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        movieDbApi = mockk()
        moviesRepository = MoviesRepositoryImpl(movieDbApi, testCoroutineRule.dispatcher)
        getNowPlayingMoviesUseCase = GetNowPlayingMoviesUseCaseImpl(moviesRepository)
    }

    @Test
    fun `invoke returns success with MovieResponse`() = runTest {
        val movieResponse = MovieResponse(
            results = listOf(movieInstanceExample),
            dates = dateRangeExample,
            page = 10,
            totalPages = 11,
            totalResults = 12
        )
        val response = Response.success(movieResponse)

        coEvery { movieDbApi.getNowPlayingMovies(API_KEY) } returns response

        val result: Result<MovieResponse> = getNowPlayingMoviesUseCase(API_KEY)
        val expected: Result<MovieResponse> = Result.success(movieResponse)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns failure on generic error`() = runTest {
        val exception = Exception("Generic Exception")
        coEvery { movieDbApi.getNowPlayingMovies(API_KEY) } throws exception

        val result: Result<MovieResponse> = getNowPlayingMoviesUseCase(API_KEY)
        val expected: Result<MovieResponse> = Result.failure(ApiError.GenericError(exception))

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns failure on HTTP error`() = runTest {
        val errorCode = 404
        val errorMessage = "Not Found"
        val errorResponseBody = errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        val response: Response<MovieResponse> = Response.error(errorCode, errorResponseBody)

        coEvery { movieDbApi.getNowPlayingMovies(API_KEY) } returns response

        val result: Result<MovieResponse> = getNowPlayingMoviesUseCase(API_KEY)
        val expected: Result<MovieResponse> = Result.failure(
            ApiError.HttpError(
                code = errorCode,
                errorMessage = errorMessage
            )
        )
        assertEquals(expected, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    class TestCoroutineRule(
        val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            super.starting(description)
            Dispatchers.setMain(dispatcher)
        }

        override fun finished(description: Description) {
            super.finished(description)
            Dispatchers.resetMain()
        }
    }

    private companion object {
        const val API_KEY = "api-key"
    }
}
