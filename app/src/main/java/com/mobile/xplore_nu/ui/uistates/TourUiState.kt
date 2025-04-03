package com.mobile.xplore_nu.ui.uistates

import com.mobile.domain.models.PointOfInterest

sealed class TourUiState {

    object StartTour : TourUiState() {
        var isButtonEnabled = true
    }

    object GettingStarted: TourUiState() {
        var isButtonEnabled = true
    }

    object RedirectToStart : TourUiState() {
        var isButtonEnabled = false
    }

    object MilestoneReached : TourUiState() {
        var isButtonEnabled = true
    }

    data class NavigateToNextStop(
        val currentBuilding: PointOfInterest,
        val nextBuilding: PointOfInterest,
        var isButtonEnabled: Boolean = false
    ) : TourUiState()

    object EndMilestoneReached : TourUiState() {
        var isButtonEnabled = true
    }
}