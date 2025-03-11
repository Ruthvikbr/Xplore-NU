package com.mobile.xplore_nu

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.mobile.xplore_nu.ui.screens.auth.AuthViewModel
import com.mobile.xplore_nu.ui.screens.auth.LoginPage
import com.mobile.xplore_nu.ui.screens.auth.RegistrationPage
import com.mobile.xplore_nu.ui.screens.tour.TourPage
import com.mobile.xplore_nu.ui.screens.tour.TourViewModel
import com.mobile.xplore_nu.ui.theme.XploreNUTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val tourViewModel: TourViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        setContent {
            XploreNUTheme {
                val navController = rememberNavController()
                val isLoggedIn by  authViewModel.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)

                //splashScreen.setKeepOnScreenCondition { isLoggedIn == false }

                Log.d("MainActivity", "Login state observed in UI: $isLoggedIn")

                LaunchedEffect(isLoggedIn) {
                    val destination = if (isLoggedIn) "home" else "auth"
                    Log.d("MainActivity", "Navigating to: $destination")
                    navController.navigate(destination) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "splash" // Placeholder, handled by LaunchedEffect
                ) {
                    composable("splash") {  }
                    authNavigation(navController, authViewModel)
                    homeNavigation(navController, tourViewModel)
                }
            }
        }
    }
}

private fun NavGraphBuilder.authNavigation(navController: NavController, viewModel: AuthViewModel) {
    navigation(startDestination = "login", route = "auth") {
        composable("login") {
            val state by viewModel.loginState.collectAsState()
            val loginStatus by viewModel.loginStatus.collectAsState()
            LoginPage(
                onRegisterButtonClicked = { navController.navigate("register") },
                loginState = state,
                onBackButtonClicked = navController::popBackStack,
                onEmailUpdated = viewModel::updateEmailForLogin,
                onPasswordUpdated = viewModel::updatePasswordForLogin,
                loginStatus = loginStatus,
                onLoginButtonClicked = viewModel::loginUser,
                navigateToHomeScreen = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onForgotPasswordButtonClicked = {}
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
    }
}

private fun NavGraphBuilder.homeNavigation(navController: NavController, viewModel: TourViewModel) {
    navigation(startDestination = "tour", route = "home") {
        composable("tour") {
            TourPage(onButtonClicked = {
                viewModel.logoutUser()
                navController.navigate("auth") {
                    popUpTo("login") { inclusive = true }
                }
            })
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