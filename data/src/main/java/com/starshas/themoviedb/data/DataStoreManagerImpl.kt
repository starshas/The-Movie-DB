package com.starshas.themoviedb.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DataStoreManager {
    private val Context.dataStore by preferencesDataStore(name = "movie_preferences")

    private fun getFavoriteMoviePreferenceKey(movieId: Int): Preferences.Key<Boolean> {
        val favoriteKeyPrefix = "favorite_movie_"
        return booleanPreferencesKey(favoriteKeyPrefix + movieId)
    }

    override suspend fun setFavoriteMovie(movieId: Int, isFavorite: Boolean) {
        val preferenceKey = getFavoriteMoviePreferenceKey(movieId)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = isFavorite
        }
    }

    override fun isFavoriteMoviesFlow(movieId: Int): Flow<Boolean> {
        val preferenceKey = getFavoriteMoviePreferenceKey(movieId)
        return context.dataStore.data
            .map { preferences ->
                preferences[preferenceKey] ?: false
            }
    }
}
