package com.mobile.xplore_nu.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.utils.Resource
import com.mobile.domain.utils.Status
import com.mobile.xplore_nu.ui.components.AppNameHeader
import com.mobile.xplore_nu.ui.components.HuskyLogoImage
import com.mobile.xplore_nu.ui.components.LeftChevron
import com.mobile.xplore_nu.ui.components.OutlinedTextFieldComponent
import com.mobile.xplore_nu.ui.components.RedButton
import com.mobile.xplore_nu.ui.theme.fontFamily
import com.mobile.xplore_nu.ui.uistates.RegisterState

@Composable
fun RegistrationPage(
    registerState: RegisterState,
    onBackButtonClicked: () -> Unit,
    onRegisterButtonClicked: (state: RegisterState) -> Unit,
    onFirstNameUpdated: (firstName: String) -> Unit,
    onLastNameUpdated: (lastName: String) -> Unit,
    onEmailUpdated: (email: String) -> Unit,
    onPasswordUpdated: (password: String) -> Unit,
    onConfirmPasswordUpdated: (confirmPassword: String) -> Unit,
    registrationStatus: Resource<UserRegisterResponse>,
    navigateToHomeScreen: () -> Unit,
) {

    val focusRequester = remember {
        FocusRequester()
    }


    val focusManager: FocusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    val context = LocalContext.current

    LaunchedEffect(registrationStatus) {
        when (registrationStatus.status) {
            Status.SUCCESS ->  {
                navigateToHomeScreen()
            }
            Status.ERROR ->  {
                Toast.makeText(context, registrationStatus.message, Toast.LENGTH_LONG).show()
            }
            Status.LOADING ->  {
                // Idle state
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LeftChevron(onClick = onBackButtonClicked)
        }
        AppNameHeader()
        HuskyLogoImage()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sign up",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000)
                )
            )
        }
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp)
                .focusRequester(focusRequester),
            label = "First Name",
            value = registerState.firstName,
            onValueChange = onFirstNameUpdated,
            isError = registerState.firstName.isNotEmpty() && !registerState.isFirstNameValid,
            errorMessage = "Enter a valid first name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            enabled = !registerState.isLoading
        )
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp),
            label = "Last Name",
            value = registerState.lastName,
            onValueChange = onLastNameUpdated,
            isError = registerState.lastName.isNotEmpty() && !registerState.isLastNameValid,
            errorMessage = "Enter a valid last name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            enabled = !registerState.isLoading
        )
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            label = "Email ID",
            value = registerState.email,
            onValueChange = onEmailUpdated,
            isError = registerState.email.isNotEmpty() && !registerState.isEmailValid,
            errorMessage = "Enter a valid email ID",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            enabled = !registerState.isLoading
        )
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            label = "Password",
            value = registerState.password,
            onValueChange = onPasswordUpdated,
            isError = registerState.password.isNotEmpty() && !registerState.isPasswordValid,
            errorMessage = "Enter a valid password",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !registerState.isLoading
        )
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
            label = "Confirm Password",
            value = registerState.confirmPassword,
            onValueChange = onConfirmPasswordUpdated,
            isError = !(registerState.isConfirmPasswordValid || registerState.doPasswordsMatch),
            errorMessage = "Enter a valid confirm password",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !registerState.isLoading
        )
        RedButton(
            label = "Register", modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(start = 24.dp, end = 24.dp), onClick = {
                onRegisterButtonClicked(registerState)
            },
            enabled = registerState.canRegister,
            isLoading = registerState.isLoading
        )
    }
}