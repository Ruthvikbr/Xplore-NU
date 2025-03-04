package com.mobile.xplore_nu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.mobile.xplore_nu.ui.screens.auth.AuthViewModel
import com.mobile.xplore_nu.ui.screens.auth.LoginPage
import com.mobile.xplore_nu.ui.screens.auth.RegistrationPage
import com.mobile.xplore_nu.ui.theme.XploreNUTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XploreNUTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "auth") {
                    navigation(startDestination = "register", route = "auth") {
                        composable("login") {
                            val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                            LoginPage(onRegisterButtonClicked = { navController.navigate("register") })
                        }
                        composable("register") {
                            val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                            val state = viewModel.registerState.collectAsState()
                            RegistrationPage(
                                state.value,
                                onBackButtonClicked = navController::popBackStack,
                                onRegisterButtonClicked = viewModel::registerUser,
                                onFullNameUpdated = viewModel::updateFullName,
                                onEmailUpdated = viewModel::updateEmail,
                                onPasswordUpdated = viewModel::updatePassword,
                                onConfirmPasswordUpdated = viewModel::updateConfirmPassword,
                            )
                        }
                    }
                }
            }
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
