package com.mobile.xplore_nu.ui.screens.event

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.mobile.domain.models.Event
import com.mobile.xplore_nu.ui.theme.XploreNUTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class EventsPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun eventsPage_displaysEvents() {
        val events = listOf(
            Event(
                name = "Test Event 1",
                date = Calendar.getInstance().apply { set(2024, Calendar.NOVEMBER, 10) }.time,
                time = "2:00 PM",
                location = "Location 1",
                description = "Description for Test Event 1",
                images = listOf("image1.jpg", "image2.jpg")
            ),
            Event(
                name = "Test Event 2",
                date = Calendar.getInstance().apply { set(2024, Calendar.DECEMBER, 15) }.time,
                time = "7:00 PM",
                location = "Location 2",
                description = "Description for Test Event 2",
                images = listOf("image3.jpg")
            )
        )
        val eventsFlow = MutableStateFlow(events)

        composeTestRule.setContent {
            XploreNUTheme {
                val navController = rememberNavController()
                val eventsState by eventsFlow.collectAsState()
                EventsPage(eventsState, navController)
            }
        }

        composeTestRule.onNodeWithText("Upcoming Events").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Event 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Event 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("November 10, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("December 15, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("Location 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Location 2").assertIsDisplayed()
    }
}