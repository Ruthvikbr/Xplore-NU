package com.mobile.domain.usecases

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class FetchPointsOfInterestUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke() = repository.getPointOfInterestMarkers()
}