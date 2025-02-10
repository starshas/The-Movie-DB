package com.starshas.themoviedb.config

import com.starshas.themoviedb.BuildConfig
import com.starshas.themoviedb.common.ApiKeyProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppApiKeyProvider
    @Inject
    constructor() : ApiKeyProvider {
        override val apiKey: String
            get() = BuildConfig.API_KEY
    }
