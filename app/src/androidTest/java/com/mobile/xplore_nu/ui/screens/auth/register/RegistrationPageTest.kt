package com.mobile.xplore_nu.ui.screens.auth.register

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mobile.domain.utils.Resource
import com.mobile.xplore_nu.ui.components.common.RedButton
import com.mobile.xplore_nu.ui.screens.auth.login.LoginPage
import com.mobile.xplore_nu.ui.theme.XploreNUTheme
import com.mobile.xplore_nu.ui.uistates.LoginState
import com.mobile.xplore_nu.ui.uistates.RegisterState
import org.junit.Rule
import org.junit.Test

class RegistrationPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registrationPage_displaysAllElements() {
        composeTestRule.setContent {
            XploreNUTheme {
                RegistrationPage (
                    onRegisterButtonClicked = {},
                    onEmailUpdated = {},
                    onPasswordUpdated = {},
                    navigateToHomeScreen = {},
                    onBackButtonClicked = {},
                    onLastNameUpdated = {},
                    onFirstNameUpdated = {},
                    registrationStatus = Resource.loading(null),
                    onConfirmPasswordUpdated = {},
                    registerState = RegisterState()
                )
            }
        }

        composeTestRule.onNodeWithText("XploreNU").assertExists()
        composeTestRule.onNodeWithText("Sign up").assertExists()
        composeTestRule.onNodeWithText("First Name").assertExists()
        composeTestRule.onNodeWithText("Last Name").assertExists()
        composeTestRule.onNodeWithText("Email ID").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Confirm Password").assertExists()
        composeTestRule.onNodeWithText("Register").assertExists()
    }

    @Test
    fun registrationPage_registerButtonClicked() {
        var registerButtonClicked = false
        composeTestRule.setContent {
            RedButton(label = "Register", onClick = { registerButtonClicked = true }, enabled = true)
        }
        composeTestRule.onNodeWithText("Register").performClick()
        composeTestRule.runOnIdle {
            assert(registerButtonClicked)
        }
    }
}