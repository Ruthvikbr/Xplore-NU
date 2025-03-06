package com.mobile.data.local.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobile.data.local.mappers.toDLoginRequest
import com.mobile.data.local.mappers.toDUser
import com.mobile.data.local.mappers.toDUserRegisterBody
import com.mobile.data.local.mappers.toLoginResponse
import com.mobile.data.local.mappers.toUser
import com.mobile.data.local.mappers.toUserRegisterResponse
import com.mobile.data.local.room.Dao
import com.mobile.data.remote.UserService
import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.LoginResponse
import com.mobile.domain.models.User
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.repository.UserRepository
import com.mobile.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val userService: UserService,
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")

    override suspend fun insertUser(user: User) {
        dao.insertUser(user.toDUser())
    }

    override fun getUser(id: String): User? {
        return dao.getUser(id)?.toUser()
    }

    override suspend fun loginUser(loginRequest: LoginRequest): LoginResponse {
        val response = userService.loginUser(loginRequest.toDLoginRequest())
        return response.body()?.toLoginResponse() ?: throw Exception("Login failed")
    }

    override suspend fun registerUser(userRegisterBody: UserRegisterBody): Resource<UserRegisterResponse> {
        try {
            val response = userService.registerUser(userRegisterBody.toDUserRegisterBody())
            return if (response.isSuccessful && response.body() != null) {
                val data: UserRegisterResponse = response.body()!!.toUserRegisterResponse()

                saveAuthToken(data.token)
                setIsLoggedIn(true)

                Resource.success(data = data)
            } else {
                Resource.error(response.body()?.message ?: "Something went wrong", null)
            }
        } catch (e: Exception) {
           return Resource.error("Something went Wrong", null)
        }
    }

    override suspend fun logoutUser(): UserRegisterResponse {
        val response = userService.logoutUser()
        return response.body()?.toUserRegisterResponse() ?: throw Exception("Logout failed")
    }


    override suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    override val authToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN_KEY]
    }

    override suspend fun clearAuthToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    override suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] == true
    }
}