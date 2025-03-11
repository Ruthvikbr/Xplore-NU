package com.mobile.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.data.local.models.AuthState
import com.mobile.data.local.models.DUser

@Database(
    entities = [DUser::class, AuthState::class],
    version = 1,
    exportSchema = false
)
abstract class XploreNUDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
    abstract fun getAuthDao(): AuthDao
}