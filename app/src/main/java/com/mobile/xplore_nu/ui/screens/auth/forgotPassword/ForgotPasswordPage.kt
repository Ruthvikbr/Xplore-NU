package com.mobile.xplore_nu.ui.screens.auth.forgotPassword

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.domain.models.RequestOtpResponse
import com.mobile.domain.utils.Resource
import com.mobile.domain.utils.Status
import com.mobile.xplore_nu.ui.components.AppNameHeader
import com.mobile.xplore_nu.ui.components.HuskyLogoImage
import com.mobile.xplore_nu.ui.components.LeftChevron
import com.mobile.xplore_nu.ui.components.OutlinedTextFieldComponent
import com.mobile.xplore_nu.ui.components.RedButton
import com.mobile.xplore_nu.ui.theme.fontFamily
import com.mobile.xplore_nu.ui.uistates.ForgotPasswordState

@Composable
fun ForgotPasswordPage(
    forgotPasswordState: ForgotPasswordState,
    onBackButtonClicked: () -> Unit,
    onEmailUpdated: (email: String) -> Unit,
    onRequestOtpClicked: (email: String) -> Unit,
    requestOtpResponse: Resource<RequestOtpResponse>,
    navigateToVerifyOtpScreen: (email: String) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager: FocusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    val context = LocalContext.current

    LaunchedEffect(requestOtpResponse) {
        when (requestOtpResponse.status) {
            Status.SUCCESS -> {
                navigateToVerifyOtpScreen(forgotPasswordState.email)
            }

            Status.ERROR -> {
                Toast.makeText(context, requestOtpResponse.message, Toast.LENGTH_LONG).show()
            }

            Status.LOADING -> {
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
        Spacer(modifier = Modifier.height(20.dp))
        AppNameHeader()
        HuskyLogoImage()
        Text(
            text = "Enter email to receive OTP",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFF000000)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                .focusRequester(focusRequester),
            label = "Email ID",
            value = forgotPasswordState.email,
            onValueChange = onEmailUpdated,
            isError = forgotPasswordState.email.isNotEmpty() && !forgotPasswordState.isEmailValid,
            errorMessage = "Enter a valid email ID",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            enabled = !forgotPasswordState.isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        RedButton(
            label = "Get OTP", modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(start = 24.dp, end = 24.dp),
            onClick = {
                onRequestOtpClicked(forgotPasswordState.email)
            },
            enabled = forgotPasswordState.canRequestOtp,
            isLoading = forgotPasswordState.isLoading
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    ForgotPasswordPage(
        forgotPasswordState = ForgotPasswordState(
            canRequestOtp = true,
            isLoading = false
        ),
        onBackButtonClicked = {},
        onEmailUpdated = {},
        onRequestOtpClicked = {},
        navigateToVerifyOtpScreen = {},
        requestOtpResponse = Resource.success<RequestOtpResponse>(
            RequestOtpResponse("")
        )
    )
}