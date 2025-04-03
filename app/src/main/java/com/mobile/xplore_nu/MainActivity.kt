package com.mobile.xplore_nu

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mobile.domain.models.User
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.ForgotPasswordPage
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.ForgotPasswordViewModel
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.OtpVerificationPage
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.PasswordResetPage
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.ResetPasswordConfirmationPage
import com.mobile.xplore_nu.ui.screens.auth.login.LoginPage
import com.mobile.xplore_nu.ui.screens.auth.login.LoginViewModel
import com.mobile.xplore_nu.ui.screens.auth.register.RegisterViewModel
import com.mobile.xplore_nu.ui.screens.auth.register.RegistrationPage
import com.mobile.xplore_nu.ui.screens.event.EventDetailsPage
import com.mobile.xplore_nu.ui.screens.event.EventViewModel
import com.mobile.xplore_nu.ui.screens.event.EventsPage
import com.mobile.xplore_nu.ui.screens.profile.ProfileScreen
import com.mobile.xplore_nu.ui.screens.profile.ProfileViewModel
import com.mobile.xplore_nu.ui.screens.tour.TopLevelRoute
import com.mobile.xplore_nu.ui.screens.tour.TourPage
import com.mobile.xplore_nu.ui.screens.tour.TourViewModel
import com.mobile.xplore_nu.ui.theme.XploreNUTheme
import dagger.hilt.android.AndroidEntryPoint
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        setContent {
            XploreNUTheme {
                val navController = rememberNavController()
                val isLoggedIn by splashViewModel.isLoggedIn.collectAsState()

                splashScreen.setKeepOnScreenCondition { isLoggedIn == null }

                val topLevelRouteNames = listOf("tour", "events", "chatbot", "account")
                val topLevelRoutes = listOf(
                    TopLevelRoute("Home", "tour", Icons.Default.Home),
                    TopLevelRoute("Events", "event", ImageVector.vectorResource(id = R.drawable.calendar_icon)),
                    TopLevelRoute("Chatbot", "chatbot", ImageVector.vectorResource(id = R.drawable.chatbot_icon)),
                    TopLevelRoute("Account", "account", Icons.Default.Person)
                )

                Scaffold (
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        if (currentRoute in topLevelRouteNames) { // Show bottom bar only for these routes
                            BottomNavigation (
                                backgroundColor = Color.White
                            ) {
                                topLevelRoutes.forEach { route ->
                                    BottomNavigationItem(
                                        icon = { Icon(
                                            route.icon,
                                            contentDescription = route.name,
                                            tint = if (currentRoute == route.route) Color.Red else Color.Black
                                        ) },
                                        label = { Text(
                                            route.name,
                                            color = if (currentRoute == route.route) Color.Red else Color.Black
                                        ) },
                                        selected = currentRoute == route.route,
                                        selectedContentColor = Color.Red,
                                        unselectedContentColor = Color.Black,
                                        onClick = {
                                            navController.navigate(route.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = isLoggedIn?.let { if (it) "home" else "auth" }
                            ?: "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash") { }
                        authNavigation(navController)
                        homeNavigation(navController)
                        eventNavigation(navController)
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.authNavigation(navController: NavController) {
    navigation(startDestination = "login", route = "auth") {
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val state by loginViewModel.loginState.collectAsState()
            val loginStatus by loginViewModel.loginStatus.collectAsState()
            LoginPage(
                onRegisterButtonClicked = { navController.navigate("register") },
                loginState = state,
                onEmailUpdated = loginViewModel::updateEmailForLogin,
                onPasswordUpdated = loginViewModel::updatePasswordForLogin,
                loginStatus = loginStatus,
                onLoginButtonClicked = loginViewModel::loginUser,
                navigateToHomeScreen = {
                    navController.navigate("home")
                },
                onForgotPasswordButtonClicked = {
                    navController.navigate("forgot_password")
                }
            )
        }
        composable("register") {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            val state by registerViewModel.registerState.collectAsState()
            val registrationStatus by registerViewModel.registerStatus.collectAsState()
            RegistrationPage(
                state,
                onBackButtonClicked = navController::popBackStack,
                onRegisterButtonClicked = registerViewModel::registerUser,
                onFirstNameUpdated = registerViewModel::updateFirstName,
                onLastNameUpdated = registerViewModel::updateLastName,
                onEmailUpdated = registerViewModel::updateEmail,
                onPasswordUpdated = registerViewModel::updatePassword,
                onConfirmPasswordUpdated = registerViewModel::updateConfirmPassword,
                registrationStatus,
                navigateToHomeScreen = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password") {
            val forgotPasswordViewModel = it.sharedViewModel<ForgotPasswordViewModel>(navController)
            val state by forgotPasswordViewModel.forgotPasswordState.collectAsState()
            val requestOtpStatus by forgotPasswordViewModel.requestOtpStatus.collectAsState()
            ForgotPasswordPage(
                forgotPasswordState = state,
                onBackButtonClicked = navController::popBackStack,
                onEmailUpdated = forgotPasswordViewModel::forgotPasswordEmailUpdated,
                onRequestOtpClicked = forgotPasswordViewModel::requestOtp,
                requestOtpResponse = requestOtpStatus,
                navigateToVerifyOtpScreen = { email ->
                    forgotPasswordViewModel.resetRequestOtpStatus()
                    val encodedEmail = Uri.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("otp_verification?email=$encodedEmail")
                }
            )
        }
        composable(
            "otp_verification?email={email}",
            arguments = listOf(navArgument("email") { defaultValue = ""; nullable = false })
        ) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<ForgotPasswordViewModel>(navController)
            val email = backStackEntry.arguments?.getString("email")
            val otpState by viewModel.otpState.collectAsState()
            val verifyOtpStatus by viewModel.verifyOtpStatus.collectAsState()
            val resendOtpStatus by viewModel.resendOtpStatus.collectAsState()

            OtpVerificationPage(
                onBackButtonClicked = {
                    navController.navigate("login") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onVerifyOtpButtonClicked = { otp ->
                    viewModel.verifyOtp(email!!, otp)
                },
                otpState,
                onResendOtpButtonClicked = {
                    viewModel.resendOtp(email!!)
                },
                onOtpValueChanged = viewModel::updateOtp,
                verifyOtpStatus,
                resendOtpStatus,
                navigateToPasswordResetScreen = {
                    viewModel.resetVerifyOtpStatus()
                    navController.navigate("password_reset?email=$email")
                }
            )
        }

        composable(
            "password_reset?email={email}",
            arguments = listOf(navArgument("email") { defaultValue = ""; nullable = false })
        ) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<ForgotPasswordViewModel>(navController)
            val email = backStackEntry.arguments?.getString("email")
            val resetPasswordStatus by viewModel.resetPasswordStatus.collectAsState()
            val resetPasswordState by viewModel.resetPasswordState.collectAsState()

            PasswordResetPage(
                resetPasswordState,
                onBackButtonClicked = {
                    navController.navigate("login") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onResetButtonClicked = {
                    viewModel.resetPassword(email!!, resetPasswordState.password)
                },
                navigateToResetConfirmationPage = {
                    viewModel.resetResendOtpStatus()
                    navController.navigate("reset_confirmation")
                },
                resetPasswordStatus,
                onPasswordUpdated = viewModel::updateNewPassword,
                onConfirmPasswordUpdated = viewModel::updateNewConfirmPassword,
            )
        }
        composable("reset_confirmation") {
            ResetPasswordConfirmationPage(
                onBackToLoginButtonClicked = {
                    navController.navigate("login") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onBackButtonClicked = {
                    navController.navigate("login") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
            )
        }
    }
}

private fun NavGraphBuilder.homeNavigation(navController: NavController) {
    navigation(startDestination = "tour", route = "home") {
        composable("tour") {
            val viewModel = it.sharedViewModel<TourViewModel>(navController)
            val points by viewModel.points.collectAsState()
            val mapUiState by viewModel.uiState.collectAsState()
            val directions by viewModel.directions.collectAsState()
            TourPage(
                points ?: emptyList(),
                updateUserLocation = viewModel::updateUserLocation,
                startTour = viewModel::startTour,
                mapUiState,
                directions
            )
        }
        composable("chatbot") {}
        composable("account") {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val user by produceState<User?>(initialValue = null) {
                value = profileViewModel.getUser()
            }
            ProfileScreen(userName = (user?.firstName + " " + user?.lastName), userEmail = user?.email) {
                profileViewModel.logout()
                navController.navigate("login") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }
    }
}

private fun NavGraphBuilder.eventNavigation(navController: NavController) {
    navigation(startDestination = "events", route="event") {
        composable("events") {
            val eventViewModel: EventViewModel = hiltViewModel()
            val events by eventViewModel.events.collectAsState()
            EventsPage(events, navController)
        }
        composable("details/{eventImages}/{eventName}/{eventDate}/{eventLocation}/{eventDescription}") { navBackStackEntry ->
            val encodedEventImageURLs = navBackStackEntry.arguments?.getString("eventImages")
            val eventImages = Uri.decode(encodedEventImageURLs).split(",").filter { it.isNotEmpty() }
            val eventName = navBackStackEntry.arguments?.getString("eventName")
            val eventDate = navBackStackEntry.arguments?.getString("eventDate")
            val eventLocation = navBackStackEntry.arguments?.getString("eventLocation")
            val eventDescription = navBackStackEntry.arguments?.getString("eventDescription")

            EventDetailsPage(
                eventImages = eventImages,
                eventName = eventName,
                eventDate = eventDate ,
                eventLocation = eventLocation,
                eventDescription = eventDescription
            )
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