package com.starshas.themoviedb.tests

import com.starshas.themoviedb.data.MovieDbApi
import com.starshas.themoviedb.data.constants.DataConstants
import com.starshas.themoviedb.data.models.DataMovieResponse
import com.starshas.themoviedb.data.repositories.MoviesRepositoryImpl
import com.starshas.themoviedb.domain.models.DomainApiError
import com.starshas.themoviedb.domain.models.DomainMoviesInfo
import com.starshas.themoviedb.domain.repositories.MoviesRepository
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCase
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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

    private val dataMovieInstanceExample = DataMovieResponse.Movie(
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

    private val domainMovieInstanceExample = DomainMoviesInfo.Movie(
        adult = false,
        backdropUrl = "${DataConstants.BASE_URL_IMAGES}/pathToBackdrop.jpg",
        genreIds = listOf(28, 12, 16),
        id = 550,
        originalLanguage = "en",
        originalTitle = "Fight Club",
        overview = "Overview",
        popularity = 0.5,
        posterUrl = "${DataConstants.BASE_URL_IMAGES}/pathToPoster.jpg",
        releaseDate = "1999-10-15",
        title = "Fight Club",
        video = false,
        voteAverage = 8.8,
        voteCount = 12345
    )

    private val dataDateRangeExample = DataMovieResponse.DatesRange(
        maximum = "2023-12-20T23:59:59-05:00",
        minimum = "2023-01-15T00:00:00-05:00"
    )

    private val domainDateRangeExample = DomainMoviesInfo.DateRange(
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
        val dataMovieResponse = DataMovieResponse(
            results = listOf(dataMovieInstanceExample),
            dates = dataDateRangeExample,
            page = 10,
            totalPages = 11,
            totalResults = 12
        )
        val expectedResponse = DomainMoviesInfo(
            results = listOf(domainMovieInstanceExample),
            dates = domainDateRangeExample,
            page = 10,
            totalPages = 11,
            totalResults = 12
        )
        val dataResponse = Response.success(dataMovieResponse)

        coEvery { movieDbApi.getNowPlayingMovies(TEST_API_KEY) } returns dataResponse

        val result: Result<DomainMoviesInfo> = getNowPlayingMoviesUseCase(TEST_API_KEY)
        assertTrue(result.isSuccess)
        val actualResponse = result.getOrNull()
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `invoke returns failure on generic error`() = runTest {
        val exception = Exception("Generic Exception")
        coEvery { movieDbApi.getNowPlayingMovies(TEST_API_KEY) } throws exception

        val result: Result<DomainMoviesInfo> = getNowPlayingMoviesUseCase(TEST_API_KEY)
        assertTrue(result.isFailure)
        val actualException = result.exceptionOrNull()
        assertNotNull(actualException)
        val genericError: DomainApiError.GenericError = actualException as DomainApiError.GenericError
        assertEquals("Generic Exception", genericError.error.message)
    }

    @Test
    fun `invoke returns failure on HTTP error`() = runTest {
        val errorCode = 404
        val errorMessage = "Not Found"
        val errorResponseBody = errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        val dataResponse: Response<DataMovieResponse> = Response.error(errorCode, errorResponseBody)
        coEvery { movieDbApi.getNowPlayingMovies(TEST_API_KEY) } returns dataResponse

        val result: Result<DomainMoviesInfo> = getNowPlayingMoviesUseCase(TEST_API_KEY)
        assertTrue(result.isFailure)
        val actualException = result.exceptionOrNull()
        assertNotNull(actualException)
        val httpError = actualException as DomainApiError.HttpError
        assertEquals(errorCode, httpError.code)
        assertEquals(errorMessage, httpError.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    class TestCoroutineRule(
        val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            super.starting(description)
            kotlinx.coroutines.Dispatchers.setMain(dispatcher)
        }
        override fun finished(description: Description) {
            super.finished(description)
            kotlinx.coroutines.Dispatchers.resetMain()
        }
    }

    private companion object {
        const val TEST_API_KEY = BuildConfig.TEST_API_KEY // Can be any for the current tests
    }
}
