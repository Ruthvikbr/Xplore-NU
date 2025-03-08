package com.mobile.xplore_nu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.mobile.xplore_nu.ui.screens.auth.AuthViewModel
import com.mobile.xplore_nu.ui.screens.auth.ForgotPasswordPage
import com.mobile.xplore_nu.ui.screens.auth.LoginPage
import com.mobile.xplore_nu.ui.screens.auth.OtpVerificationPage
import com.mobile.xplore_nu.ui.screens.auth.PasswordResetPage
import com.mobile.xplore_nu.ui.screens.auth.RegistrationPage
import com.mobile.xplore_nu.ui.screens.tour.TourPage
import com.mobile.xplore_nu.ui.theme.XploreNUTheme
import com.mobile.xplore_nu.ui.uistates.ResetPasswordState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        setContent {
            XploreNUTheme {
                val navController = rememberNavController()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                splashScreen.setKeepOnScreenCondition { isLoggedIn == null }

                LaunchedEffect(isLoggedIn) {
                    isLoggedIn?.let { loggedIn ->
                        val destination = if (loggedIn) "home" else "auth"
                        navController.navigate(destination) {
                            popUpTo(if (loggedIn) "auth" else "home") { inclusive = true }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = isLoggedIn?.let { if (it) "home" else "auth" }
                        ?: "splash" // Placeholder, handled by LaunchedEffect
                ) {
                    composable("splash") { }
                    authNavigation(navController, authViewModel)
                    homeNavigation(navController)
                }
            }
        }
    }
}

private fun NavGraphBuilder.authNavigation(navController: NavController, viewModel: AuthViewModel) {
    navigation(startDestination = "login", route = "auth") {
        composable("login") {
            LoginPage(
                onRegisterButtonClicked = { navController.navigate("register") },
                onForgotPasswordClicked = { navController.navigate("forgot_password") },
            )
        }
        composable("register") {
            val state by viewModel.registerState.collectAsState()
            val registrationStatus by viewModel.registerStatus.collectAsState()
            RegistrationPage(
                state,
                onBackButtonClicked = navController::popBackStack,
                onRegisterButtonClicked = viewModel::registerUser,
                onFirstNameUpdated = viewModel::updateFirstName,
                onLastNameUpdated = viewModel::updateLastName,
                onEmailUpdated = viewModel::updateEmail,
                onPasswordUpdated = viewModel::updatePassword,
                onConfirmPasswordUpdated = viewModel::updateConfirmPassword,
                registrationStatus,
                navigateToHomeScreen = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("forgot_password") {
            val state by viewModel.forgotPasswordState.collectAsState()
            ForgotPasswordPage(
                forgotPasswordState = state,
                onBackButtonClicked = navController::popBackStack,
                onEmailUpdated = viewModel::forgotPasswordEmailUpdated,
                onRequestOtpClicked = {
                    navController.navigate("otp_verification")
                }
            )
        }
        composable("otp_verification") {
            OtpVerificationPage(
                onBackButtonClicked = navController::popBackStack,
                onVerifyOtpButtonClicked = {
                    navController.navigate("password_reset")
                })
        }

        composable("password_reset") {
            PasswordResetPage(
                resetPasswordState = ResetPasswordState(),
                onBackButtonClicked = navController::popBackStack,
                onResetButtonClicked = {
                    navController.navigate("reset_confirmation")
                })
        }
        composable("reset_confirmation") {

        }
    }
}

private fun NavGraphBuilder.homeNavigation(navController: NavController) {
    navigation(startDestination = "tour", route = "home") {
        composable("tour") {
            TourPage()
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}