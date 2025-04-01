package com.mobile.xplore_nu.ui.screens.tour

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.models.PointOfInterest
import com.mobile.domain.usecases.FetchPointsOfInterestUseCase
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

    fun getPoints() {
        viewModelScope.launch(Dispatchers.IO) {
            val points = fetchPointsOfInterestUseCase.invoke().data?.points?.sortedBy { it.ord }
            Log.d("viewmodel", "$points")
            _points.emit(points)
        }
    }
}