package com.mobile.xplore_nu.ui.screens.tour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.usecases.AuthStatusUseCase
import com.mobile.domain.usecases.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val authStatusUseCase: AuthStatusUseCase
): ViewModel() {

    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            //authStatusUseCase.updateLoginState(false)
            logoutUserUseCase.invoke()
        }
    }
}