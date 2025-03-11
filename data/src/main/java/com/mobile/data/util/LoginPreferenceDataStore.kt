package com.mobile.data.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class LoginPreferenceDataStore @Inject constructor(
    @ApplicationContext private val context: Context
){

    private val dataStore = context.dataStore

    companion object {
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN_KEY] ?: false }
        .distinctUntilChanged()

    suspend fun updateLoginState(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }
}