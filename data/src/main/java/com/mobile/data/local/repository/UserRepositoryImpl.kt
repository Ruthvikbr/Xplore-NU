package com.mobile.data.local.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobile.data.local.mappers.toDLoginRequest
import com.mobile.data.local.mappers.toDUser
import com.mobile.data.local.mappers.toDUserRegisterBody
import com.mobile.data.local.mappers.toLoginResponse
import com.mobile.data.local.mappers.toLogoutResponse
import com.mobile.data.local.mappers.toUser
import com.mobile.data.local.mappers.toUserRegisterResponse
import com.mobile.data.local.models.AuthState
import com.mobile.data.local.models.DUser
import com.mobile.data.local.room.AuthDao
import com.mobile.data.local.room.Dao
import com.mobile.data.remote.UserService
import com.mobile.data.util.LoginPreferenceDataStore
import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.LoginResponse
import com.mobile.domain.models.LogoutResponse
import com.mobile.domain.models.User
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.repository.UserRepository
import com.mobile.domain.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UserRepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val authDao: AuthDao,
    private val userService: UserService,
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

    override val isLoggedIn = authDao.observeAuthState()

    override suspend fun insertUser(user: User) {
        dao.insertUser(user.toDUser())
    }

    override fun getUser(id: String): User? {
        return dao.getUser(id)?.toUser()
    }

    override suspend fun loginUser(loginRequest: LoginRequest): Resource<LoginResponse> {
        try {
            val response = userService.loginUser(loginRequest.toDLoginRequest())
            return if (response.isSuccessful && response.body()!=null) {
                val data: LoginResponse = response.body()!!.toLoginResponse()
                updateLoginState(true)
                saveAuthToken(data.token)


                val user = DUser(data.user.id, data.user.firstName, data.user.lastName, data.user.email, data.user.role)
                insertUser(user.toUser())

                Resource.success(data = data)
            } else {
                Resource.error(response.body()?.message ?: "Something went wrong", null)
            }
        } catch (e: Exception) {
            return Resource.error("Something went wrong", null)
        }
    }

    override suspend fun registerUser(userRegisterBody: UserRegisterBody): Resource<UserRegisterResponse> {
        try {
            val response = userService.registerUser(userRegisterBody.toDUserRegisterBody())
            return if (response.isSuccessful && response.body() != null) {
                val data: UserRegisterResponse = response.body()!!.toUserRegisterResponse()

                saveAuthToken(data.token)
                updateLoginState(true)

                val user = DUser(data.user.id, data.user.firstName, data.user.lastName, data.user.email, data.user.role)
                insertUser(user.toUser())

                Resource.success(data = data)
            } else {
                Resource.error(response.body()?.message ?: "Something went wrong", null)
            }
        } catch (e: Exception) {
           return Resource.error("Something went Wrong"+e.message, null)
        }
    }

    override suspend fun logoutUser(): Resource<LogoutResponse> {
        try {
            val response = userService.logoutUser()
            return if (response.isSuccessful && response.body() != null) {
                val data: LogoutResponse = response.body()!!.toLogoutResponse()

                updateLoginState(false)
                clearAuthToken()

                dao.deleteUsers()

                Resource.success(data = data)
            } else {
                Resource.error(response.body()?.message ?: "Something went wrong", null)
            }
        } catch (e: Exception) {
            return Resource.error("Something went Wrong"+e.message, null)
        }
//        val response = userService.logoutUser()
//        return response.body()?.toUserRegisterResponse() ?: throw Exception("Logout failed")
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

    override suspend fun updateLoginState(isLoggedIn: Boolean) {
        authDao.setAuthState(AuthState(isLoggedIn = isLoggedIn))
        Log.d("AuthRepository", "updateLoginState set to: $isLoggedIn")
    }

    override fun observeAuthState(): Flow<Boolean> {
        return authDao.observeAuthState().onEach { isLoggedIn ->
            Log.d("AuthRepository", "observeAuthState emitted: $isLoggedIn")
        }
    }
}