package com.mobile.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.data.local.models.DUser

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: DUser)

    @Query("SELECT * FROM DUser WHERE id=:id")
    fun getUser(id: String): DUser?
}