package com.mobile.xplore_nu.ui.screens.tour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mobile.domain.models.PointOfInterest
import com.mobile.domain.usecases.FetchPointsOfInterestUseCase
import com.mobile.xplore_nu.ui.uistates.TourUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourViewModel @Inject constructor(
    private val fetchPointsOfInterestUseCase: FetchPointsOfInterestUseCase,
) : ViewModel() {

    private val _points = MutableStateFlow<List<PointOfInterest>>(emptyList())
    val points: StateFlow<List<PointOfInterest>> = _points.asStateFlow()

    private val _uiState = MutableStateFlow<TourUiState>(TourUiState.StartTour)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val points = fetchPointsOfInterestUseCase.invoke().data?.points
            points?.let {
                _points.emit(points)
            }
        }
    }

    fun startTour(point: Point) {
        if (_uiState.value == TourUiState.StartTour && _points.value.isNotEmpty()) {
            val tourStartingLocation = _points.value[0]

            val startingLat = point.coordinates()[1]
            val startingLong = point.coordinates()[0]

            if (startingLat != null && startingLong != null) {
                val isUserAtStart = isUserAtStartLocation(
                    startingLat, startingLong, tourStartingLocation.lat, tourStartingLocation.long
                )

                if (isUserAtStart) {
                    _uiState.value = TourUiState.DisplayStartTourInfo(startingLat, startingLong)
                } else {
                    _uiState.value = TourUiState.HeadingToStartLocation(startingLat, startingLong)
                }
            }
        }
    }

    fun onEvent(newState: TourUiState) {
        viewModelScope.launch {
            _uiState.value = newState
        }
    }

    private fun isUserAtStartLocation(
        userLat: Double, userLong: Double, startLat: Double, startLong: Double
    ): Boolean {
        val threshold = 2.0
        return calculateDistance(userLat, userLong, startLat, startLong) <= threshold
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0 // meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(
            Math.toRadians(lat2)
        ) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }


}