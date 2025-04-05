package com.mobile.xplore_nu.ui.screens.auth.login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.mobile.domain.utils.Resource
import com.mobile.xplore_nu.ui.theme.XploreNUTheme
import com.mobile.xplore_nu.ui.uistates.LoginState
import org.junit.Rule
import org.junit.Test

class LoginPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginPage_displaysAllElements() {
        composeTestRule.setContent {
            XploreNUTheme {
                LoginPage(
                    onRegisterButtonClicked = {},
                    loginState = LoginState(),
                    onEmailUpdated = {},
                    onPasswordUpdated = {},
                    loginStatus = Resource.loading(data = null),
                    onLoginButtonClicked = {},
                    navigateToHomeScreen = {},
                    onForgotPasswordButtonClicked = {}
                )
            }
        }

        composeTestRule.onNodeWithText("XploreNU").assertExists()
        composeTestRule.onNodeWithText("Log In").assertExists()
        composeTestRule.onNodeWithText("Email ID").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Forget Password ?").assertExists()
        composeTestRule.onNodeWithText("Login").assertExists()
        composeTestRule.onNodeWithText("Don’t have an account?\n").assertExists()
        composeTestRule.onNodeWithText("Sign Up").assertExists()
    }

    @Test
    fun loginPage_validInput_loginButtonClicked() {
        var loginButtonClicked = false
        composeTestRule.setContent {
            XploreNUTheme {
                LoginPage(
                    onRegisterButtonClicked = {},
                    loginState = LoginState(
                        email = "test@example.com",
                        password = "password123",
                        isEmailValid = true,  //  <--- Ensure email is valid
                        isPasswordValid = true, //  <--- Ensure password is valid
                        isLoading = false,       //  <--- Ensure not loading
                        canLogin = true,       //  <--- Ensure login is enabled
                    ),
                    onEmailUpdated = {},
                    onPasswordUpdated = {},
                    loginStatus = Resource.loading(null),
                    onLoginButtonClicked = { _ -> loginButtonClicked = true },
                    navigateToHomeScreen = {},
                    onForgotPasswordButtonClicked = {}
                )
            }
        }

        // You no longer need to performTextInput because the fields are initialized
        // composeTestRule.onNodeWithText("Email ID").performTextInput("test@example.com")
        // composeTestRule.onNodeWithText("Password").performTextInput("password123")

        composeTestRule.onNodeWithText("Login").performClick()
        assert(loginButtonClicked)
    }
}