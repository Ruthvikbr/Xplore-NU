package com.mobile.xplore_nu.ui.screens.event

import com.mobile.domain.models.Event
import com.mobile.domain.models.UpcomingEventResponse
import com.mobile.domain.repository.UserRepository
import com.mobile.domain.usecases.UpcomingEventsUseCase
import com.mobile.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import java.util.Date

@ExperimentalCoroutinesApi
class EventViewModelTest {

    private lateinit var eventViewModel: EventViewModel
    private val userRepository = mock<UserRepository>()
    private val upcomingEventsUseCase = UpcomingEventsUseCase(userRepository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        eventViewModel = EventViewModel(upcomingEventsUseCase)
    }

    @Test
    fun `getUpcomingEvents success`() = runTest {
        // Given
        val events = listOf(
            Event(
                name = "Tech Conference",
                date = Date(),
                time = "10:00 AM",
                location = "Convention Center",
                description = "A conference for tech enthusiasts.",
                images = listOf("image1.jpg", "image2.jpg")
            ),
            Event(
                name = "Art Exhibition",
                date = Date(),
                time = "2:00 PM",
                location = "Art Gallery",
                description = "An exhibition of contemporary art.",
                images = emptyList()
            )
        )
        val expectedResponse = Resource.success(UpcomingEventResponse(success = true, count = 2, events = events))

        `when`(userRepository.getUpcomingEvents()).thenReturn(expectedResponse)

        // When
        eventViewModel.getUpcomingEvents()

        // Then
        val actualEvents = eventViewModel.events.value
        assertEquals(expectedResponse.data?.events, actualEvents)
    }
}