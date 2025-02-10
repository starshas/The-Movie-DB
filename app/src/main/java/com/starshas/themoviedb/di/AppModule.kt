package com.starshas.themoviedb.di

import com.starshas.themoviedb.common.ApiKeyProvider
import com.starshas.themoviedb.config.AppApiKeyProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindApiKeyProvider(appApiKeyProvider: AppApiKeyProvider): ApiKeyProvider
}
