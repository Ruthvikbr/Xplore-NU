package com.mobile.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mobile.data.local.models.DUser
import com.mobile.domain.models.User

@Dao
interface Dao {

    @Insert
    suspend fun insertUser(user: DUser)

    @Query("SELECT * FROM DUser WHERE id=:id")
    fun getUser(id: String): DUser?
}