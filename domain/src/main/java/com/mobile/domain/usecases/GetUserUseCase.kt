package com.mobile.domain.usecases

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(id: String) = repository.getUser(id)
}