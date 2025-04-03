package com.mobile.xplore_nu.ui.screens.tour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mapbox.geojson.Point
import com.mobile.domain.models.PointOfInterest
import com.mobile.domain.models.RouteResponse
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

    private val _points = MutableStateFlow<List<PointOfInterest>?>(null)
    val points: StateFlow<List<PointOfInterest>?> = _points.asStateFlow()

    private val _uiState = MutableStateFlow<TourUiState>(TourUiState.StartTour)
    val uiState = _uiState.asStateFlow()

    private val _directions = MutableStateFlow<RouteResponse?>(null)
    val directions: StateFlow<RouteResponse?> = _directions

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val points = fetchPointsOfInterestUseCase.invoke().data?.points?.sortedBy { it.ord }
            _points.emit(points)
        }
    }

    private val currentLocation = MutableStateFlow<Point?>(null)

    fun startTour() {
        if (_uiState.value == TourUiState.StartTour) {
            // Check if user location is at the start location
            val tourStartingLocation = _points.value?.get(0)

            val startingLat = currentLocation.value?.coordinates()?.get(1)
            val startingLong = currentLocation.value?.coordinates()?.get(0)

            if (startingLat != null && startingLong != null && tourStartingLocation != null) {
                val isUserAtStart = isUserAtStartLocation(
                    startingLat, startingLong, tourStartingLocation.lat, tourStartingLocation.long
                )

                if (isUserAtStart) {
                    _uiState.value = TourUiState.GettingStarted
                } else {
                    _uiState.value = TourUiState.RedirectToStart
                }
                fetchDirections()
            }

        }
    }

    fun fetchDirections() {
        viewModelScope.launch {
            val routeResponse = getDirections()
            _directions.value = routeResponse
        }
    }

    fun convertResponse(jsonResponse: String): RouteResponse? {
        val gson = Gson()
        return try {
            gson.fromJson(jsonResponse, RouteResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun getDirections(): RouteResponse? {
        val jsonResponse = """
        {"routes":[{"weight_name":"pedestrian","weight":125.236,"duration":125.454,"distance":176.401,"legs":[{"via_waypoints":[],"admins":[{"iso_3166_1_alpha3":"USA","iso_3166_1":"US"}],"weight":125.236,"duration":125.454,"steps":[],"distance":176.401,"summary":""}],"geometry":"oolaGxv{pLoAeELUAA\\}@NEFOC[MO"}],"waypoints":[{"distance":0.956,"name":"","location":[-71.089894,42.339924]},{"distance":6.425,"name":"","location":[-71.088142,42.340077]}],"code":"Ok","uuid":"xP3NvK6slujE3b0ODYXwzxs2aukPTPIjGifBBs73uZe_H1N8pl5WwQ=="}
    """.trimIndent()

        val routeResponse = convertResponse(jsonResponse)
        return routeResponse
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

    fun updateUserLocation(point: Point) {
        currentLocation.value = point
    }



}