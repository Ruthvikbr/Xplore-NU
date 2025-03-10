package com.mobile.domain.di

import com.mobile.domain.repository.UserRepository
import com.mobile.domain.usecases.GetUserUseCase
import com.mobile.domain.usecases.InsertUserUseCase
import com.mobile.domain.usecases.LoginUserUseCase
import com.mobile.domain.usecases.LogoutUserUseCase
import com.mobile.domain.usecases.RegisterUserUseCase
import com.mobile.domain.usecases.ResetPasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUserUseCase(userRepository: UserRepository) = LoginUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository) = GetUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideInsertUserUseCase(userRepository: UserRepository) = InsertUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideLogoutUserUseCase(userRepository: UserRepository) = LogoutUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(userRepository: UserRepository) = RegisterUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(userRepository: UserRepository) = ResetPasswordUseCase(userRepository)

}