package com.mobile.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.data.local.models.AuthState
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

    @Query("SELECT isLoggedIn FROM auth_state WHERE id = 1")
    fun observeAuthState(): Flow<Boolean> // Emits updates automatically

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAuthState(authState: AuthState)
}