package com.mobile.xplore_nu.ui.uistates

import com.mobile.domain.models.PointOfInterest

sealed class TourUiState {
    object InitialState : TourUiState() {
        var isButtonEnabled = true
    }

    object StartTour : TourUiState() {
        var isButtonEnabled = true
    }

    data class HeadingToStartLocation(val currentLat: Double, val currentLong: Double): TourUiState() {
        var isButtonEnabled = false
    }

    data class DisplayStartTourInfo(val currentLat: Double, val currentLong: Double): TourUiState() {
        var isButtonEnabled = false
    }

    data class DisplayBuildingInfo(val currentBuilding: PointOfInterest, val nextBuilding: PointOfInterest): TourUiState() {
        var isButtonEnabled = true
    }

    data class NavigatingToNextBuilding(val currentBuilding: PointOfInterest, val nextBuilding: PointOfInterest?): TourUiState() {
        var isButtonEnabled = false

    }

    object TourCompleted : TourUiState()
}