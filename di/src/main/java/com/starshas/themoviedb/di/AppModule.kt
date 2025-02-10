package com.starshas.themoviedb.di

import com.starshas.themoviedb.common.ApiKeyProvider
import com.starshas.themoviedb.data.DataStoreManager
import com.starshas.themoviedb.data.DataStoreManagerImpl
import com.starshas.themoviedb.data.MovieDbApi
import com.starshas.themoviedb.data.constants.DataConstants
import com.starshas.themoviedb.data.repositories.FavoritesRepositoryImpl
import com.starshas.themoviedb.data.repositories.MoviesRepositoryImpl
import com.starshas.themoviedb.domain.repositories.FavoritesRepository
import com.starshas.themoviedb.domain.repositories.MoviesRepository
import com.starshas.themoviedb.domain.usecases.GetFavoriteStatusUseCase
import com.starshas.themoviedb.domain.usecases.GetFavoriteStatusUseCaseImpl
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCase
import com.starshas.themoviedb.domain.usecases.GetNowPlayingMoviesUseCaseImpl
import com.starshas.themoviedb.domain.usecases.SetFavoriteUseCase
import com.starshas.themoviedb.domain.usecases.SetFavoriteUseCaseImpl
import com.starshas.themoviedb.domain.utils.StringProvider
import com.starshas.themoviedb.platform.StringProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        return OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(DataConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideMovieDbApi(retrofit: Retrofit): MovieDbApi =
        retrofit.create(
            MovieDbApi::class.java,
        )

    @Suppress("ktlint:standard:function-signature")
    @Singleton
    @Provides
    fun provideFavoritesRepository(
        dataStoreManager: DataStoreManager,
    ): FavoritesRepository = FavoritesRepositoryImpl(dataStoreManager)

    @Singleton
    @Provides
    fun provideMoviesRepository(
        apiKeyProvider: ApiKeyProvider,
        movieDbApi: MovieDbApi,
    ): MoviesRepository = MoviesRepositoryImpl(apiKey = apiKeyProvider.apiKey, movieDbApi)

    @Singleton
    @Provides
    fun provideGetNowPlayingMoviesUseCase(moviesRepository: MoviesRepository): GetNowPlayingMoviesUseCase =
        GetNowPlayingMoviesUseCaseImpl(moviesRepository)

    @Singleton
    @Provides
    fun provideGetFavoriteStatusUseCase(favoritesRepository: FavoritesRepository): GetFavoriteStatusUseCase =
        GetFavoriteStatusUseCaseImpl(favoritesRepository)

    @Singleton
    @Provides
    fun provideSetFavoriteUseCase(favoritesRepository: FavoritesRepository): SetFavoriteUseCase =
        SetFavoriteUseCaseImpl(favoritesRepository)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Binds
    @Singleton
    abstract fun bindDataStoreManager(impl: DataStoreManagerImpl): DataStoreManager

    @Binds
    @Singleton
    abstract fun bindStringProvider(impl: StringProviderImpl): StringProvider
}
