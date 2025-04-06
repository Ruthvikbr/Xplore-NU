package com.mobile.xplore_nu.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.models.User
import com.mobile.domain.usecases.GetUserUseCase
import com.mobile.domain.usecases.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
): ViewModel() {

    suspend fun getUser(): User? {
        return withContext(Dispatchers.IO) {
            getUserUseCase.invoke()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase.invoke()
        }
    }
}