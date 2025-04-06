package com.mobile.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobile.data.local.models.DUser
import com.mobile.data.utils.convertors.StringListConverter

@Database(
    entities = [DUser::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class XploreNUDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}