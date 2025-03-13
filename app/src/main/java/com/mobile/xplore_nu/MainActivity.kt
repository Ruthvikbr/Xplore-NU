package com.mobile.xplore_nu

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
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
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.ForgotPasswordPage
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.ForgotPasswordViewModel
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.OtpVerificationPage
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.PasswordResetPage
import com.mobile.xplore_nu.ui.screens.auth.forgotPassword.ResetPasswordConfirmationPage
import com.mobile.xplore_nu.ui.screens.auth.login.LoginPage
import com.mobile.xplore_nu.ui.screens.auth.login.LoginViewModel
import com.mobile.xplore_nu.ui.screens.auth.register.RegisterViewModel
import com.mobile.xplore_nu.ui.screens.auth.register.RegistrationPage
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

                NavHost(
                    navController = navController,
                    startDestination = isLoggedIn?.let { if (it) "home" else "auth" }
                        ?: "splash"
                ) {
                    composable("splash") { }
                    authNavigation(navController)
                    homeNavigation(navController)
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
            TourPage(
                onButtonClicked = {
                    viewModel.logoutUser()
                    navController.navigate("auth") {
                        popUpTo("login") { inclusive = true }
                    }
                }
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