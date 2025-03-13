package com.mobile.xplore_nu.ui.screens.auth.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.domain.models.AuthenticationResponse
import com.mobile.domain.utils.Resource
import com.mobile.domain.utils.Status
import com.mobile.xplore_nu.ui.components.AppNameHeader
import com.mobile.xplore_nu.ui.components.HuskyLogoImage
import com.mobile.xplore_nu.ui.components.OutlinedTextFieldComponent
import com.mobile.xplore_nu.ui.components.RedButton
import com.mobile.xplore_nu.ui.theme.fontFamily
import com.mobile.xplore_nu.ui.uistates.LoginState

@Composable
fun LoginPage(
    onRegisterButtonClicked: () -> Unit,
    loginState: LoginState,
    onEmailUpdated: (email: String) -> Unit,
    onPasswordUpdated: (password: String) -> Unit,
    loginStatus: Resource<AuthenticationResponse>,
    onLoginButtonClicked: (state: LoginState) -> Unit,
    navigateToHomeScreen: () -> Unit,
    onForgotPasswordButtonClicked: () -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager: FocusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    val context = LocalContext.current

    LaunchedEffect(loginStatus) {
        when (loginStatus.status) {
            Status.SUCCESS -> {
                navigateToHomeScreen()
            }

            Status.ERROR -> {
                Toast.makeText(context, loginStatus.message, Toast.LENGTH_LONG).show()
            }

            Status.LOADING -> {
                // Idle state
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        AppNameHeader()
        HuskyLogoImage()
        Text(
            text = "Log In",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFF000000)
            )
        )

        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                .focusRequester(focusRequester),
            label = "Email ID",
            value = loginState.email,
            onValueChange = onEmailUpdated,
            isError = loginState.email.isNotEmpty() && !loginState.isEmailValid,
            errorMessage = "Enter a valid email ID",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            enabled = !loginState.isLoading
        )
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            label = "Password",
            value = loginState.password,
            onValueChange = onPasswordUpdated,
            isError = loginState.password.isNotEmpty() && !loginState.isPasswordValid,
            errorMessage = "Enter a valid password",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !loginState.isLoading
        )
        TextButton(onClick = onForgotPasswordButtonClicked) {
            Text(
                text = "Forget Password ?",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFEF4444),
                    textAlign = TextAlign.Center,
                )
            )
        }
        RedButton(
            label = "Login",
            onClick = { onLoginButtonClicked(loginState) },
            enabled = loginState.canLogin,
            isLoading = loginState.isLoading
        )
        Column {
            Text(
                text = "Don’t have an account?\n",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    fontFamily = fontFamily,
                    color = Color(0xFF575757),
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Sign Up",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    fontFamily = fontFamily,
                    color = Color(0xFFEF4444),
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp)
                    .clickable {
                        onRegisterButtonClicked()
                    }
            )
        }
    }
}