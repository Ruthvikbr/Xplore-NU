package com.mobile.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

//    @Provides
//    @Singleton
//    fun provideLoginUserUseCase(userRepository: UserRepository) = LoginUserUseCase(userRepository)
//
//    @Provides
//    @Singleton
//    fun provideGetUserUseCase(userRepository: UserRepository) = GetUserUseCase(userRepository)
//
//    @Provides
//    @Singleton
//    fun provideInsertUserUseCase(userRepository: UserRepository) = InsertUserUseCase(userRepository)
//
//    @Provides
//    @Singleton
//    fun provideLogoutUserUseCase(userRepository: UserRepository) = LogoutUserUseCase(userRepository)
//
//    @Provides
//    @Singleton
//    fun provideRegisterUserUseCase(userRepository: UserRepository) = RegisterUserUseCase(userRepository)
//
//    @Provides
//    @Singleton
//    fun provideResetPasswordUseCase(userRepository: UserRepository) = ResetPasswordUseCase(userRepository)
//
//    @Provides
//    @Singleton
//    fun provideDataStoreUseCase(userRepository: UserRepository) = DataStoreUseCase(userRepository)

}