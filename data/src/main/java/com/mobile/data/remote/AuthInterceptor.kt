package com.mobile.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobile.data.remote.models.RefreshTokenRequest
import dagger.internal.Provider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthInterceptor @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    val userServiceProvider: Provider<UserService>

) : Interceptor {
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")


    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var response = chain.proceed(originalRequest)
        val userService = userServiceProvider.get()

        if (response.code == 403 && response.message == "Invalid token") {
            try {

                runBlocking {
                    val refreshToken = dataStore.data
                        .map { preferences ->
                            preferences[REFRESH_TOKEN_KEY]
                        }
                        .first()
                    refreshToken?.let {
                        val refreshTokenResponse =
                            userService.refreshToken(RefreshTokenRequest(refreshToken))

                        if (refreshTokenResponse.isSuccessful) {
                            val accessToken = refreshTokenResponse.body()!!.accessToken
                            dataStore.edit { preferences ->
                                preferences[AUTH_TOKEN_KEY] = accessToken
                            }

                            val newAuthenticatedRequest = originalRequest.newBuilder()
                                .header("Authorization", "Bearer $accessToken")
                                .build()
                            response.close()
                            return@let chain.proceed(newAuthenticatedRequest)
                        } else {
                            return@let response
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return response
            }
        }

        return response
    }

}