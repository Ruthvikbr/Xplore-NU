package com.mobile.xplore_nu.ui.screens.tour

import com.mapbox.geojson.Point
import com.mobile.domain.models.FetchPoiResponse
import com.mobile.domain.models.PointOfInterest
import com.mobile.domain.repository.UserRepository
import com.mobile.domain.usecases.FetchPointsOfInterestUseCase
import com.mobile.domain.utils.Resource
import com.mobile.xplore_nu.ui.screens.tour.TourViewModel
import com.mobile.xplore_nu.ui.uistates.TourUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TourViewModelTest {

    private lateinit var tourViewModel: TourViewModel

    private val repository = mock<UserRepository>()

    // Mock the entire use case so it cannot touch a null repository
    private lateinit var fetchPointsOfInterestUseCase: FetchPointsOfInterestUseCase

    private val startPoint = PointOfInterest(
        id = "1",
        lat = 42.056,
        long = -87.675,
        description = "Starting location for the tour",
        buildingName = "Building 1",
        images = listOf("image1"),
        ord = 1
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        // Default stub: whenever the use case is called, return a successful resource
        whenever(repository.getPointOfInterestMarkers()).thenReturn(
            Resource.success(
                FetchPoiResponse(points = listOf(startPoint), count = 1)
            )
        )

        fetchPointsOfInterestUseCase = FetchPointsOfInterestUseCase(repository)

        // Now create the ViewModel, which uses the mocked use case
        tourViewModel = TourViewModel(fetchPointsOfInterestUseCase)
    }

    @Test
    fun `startTour - user at start location`() = runTest {
        // Given: user is at the start location
        val userLocation = Point.fromLngLat(-87.675, 42.056)

        // When: Start tour is called
        tourViewModel.startTour(userLocation)

        // Then: UI state should be DisplayStartTourInfo
        assertEquals(
            TourUiState.DisplayStartTourInfo(42.056, -87.675),
            tourViewModel.uiState.value
        )
    }

    @Test
    fun `startTour - user not at start location`() = runTest {
        // (Optional) Overwrite the default stub if you want a different scenario:
        // whenever(fetchPointsOfInterestUseCase.invoke()).thenReturn(...)

        // Given: user is far from the start location
        val userLocation = Point.fromLngLat(-87.7, 42.1)

        // When: Start tour is called
        tourViewModel.startTour(userLocation)

        // Then: UI state should be HeadingToStartLocation
        assertEquals(
            TourUiState.HeadingToStartLocation(42.1, -87.7),
            tourViewModel.uiState.value
        )
    }

    @Test
    fun `startTour - points list is empty`() = runTest {
        // Given: we want the use case to return zero points
        whenever(fetchPointsOfInterestUseCase.invoke()).thenReturn(
            Resource.success(FetchPoiResponse(count = 0, points = emptyList()))
        )

        // Re-create the ViewModel so it picks up the new stubbed return
        tourViewModel = TourViewModel(fetchPointsOfInterestUseCase)

        val userLocation = Point.fromLngLat(-87.675, 42.056)

        // When: startTour is called
        tourViewModel.startTour(userLocation)

        // Then: UI state should remain StartTour or handle empty
        assertEquals(TourUiState.StartTour, tourViewModel.uiState.value)
        assertTrue(tourViewModel.points.first().isEmpty())
    }

    @Test
    fun `startTour - fetch points fails`() = runTest {
        // Given: we want the use case to fail
        whenever(fetchPointsOfInterestUseCase.invoke()).thenReturn(
            Resource.error("Failed to fetch points", null)
        )

        // Re-create the ViewModel so it picks up the new stubbed return
        tourViewModel = TourViewModel(fetchPointsOfInterestUseCase)

        val userLocation = Point.fromLngLat(-87.675, 42.056)

        // When
        tourViewModel.startTour(userLocation)

        // Then: UI state remains StartTour or an error state
        assertEquals(TourUiState.StartTour, tourViewModel.uiState.value)
    }
}