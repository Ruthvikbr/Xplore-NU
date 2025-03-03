package com.mobile.data.di

import android.content.Context
import androidx.room.Room
import com.mobile.data.local.repository.UserRepositoryImpl
import com.mobile.data.local.room.Dao
import com.mobile.data.local.room.XploreNUDatabase
import com.mobile.data.remote.UserService
import com.mobile.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideXploreNUDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, XploreNUDatabase::class.java, "Xplore_NU_Database").build()

    @Singleton
    @Provides
    fun provideDao(xploreNUDatabase: XploreNUDatabase) = xploreNUDatabase.getDao()

    @Singleton
    @Provides
    fun provideUserRepository(dao: Dao, userService: UserService): UserRepository = UserRepositoryImpl(dao, userService)
}