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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.domain.models.ResetPasswordResponse
import com.mobile.domain.utils.Resource
import com.mobile.domain.utils.Status
import com.mobile.xplore_nu.ui.components.common.AppNameHeader
import com.mobile.xplore_nu.ui.components.common.HuskyLogoImage
import com.mobile.xplore_nu.ui.components.common.LeftChevron
import com.mobile.xplore_nu.ui.components.common.OutlinedTextFieldComponent
import com.mobile.xplore_nu.ui.components.common.RedButton
import com.mobile.xplore_nu.ui.theme.fontFamily
import com.mobile.xplore_nu.ui.uistates.ResetPasswordState

@Composable
fun PasswordResetPage(
    resetPasswordState: ResetPasswordState,
    onBackButtonClicked: () -> Unit,
    onResetButtonClicked: () -> Unit,
    navigateToResetConfirmationPage: () -> Unit,
    resetPasswordResponse: Resource<ResetPasswordResponse>,
    onPasswordUpdated: (String) -> Unit,
    onConfirmPasswordUpdated: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager: FocusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    val context = LocalContext.current

    LaunchedEffect(resetPasswordResponse) {
        when (resetPasswordResponse.status) {
            Status.SUCCESS -> {
                navigateToResetConfirmationPage()
            }

            Status.ERROR -> {
                Toast.makeText(context, resetPasswordResponse.message, Toast.LENGTH_LONG).show()
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
            text = "Reset Password",
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
            label = "Password",
            value = resetPasswordState.password,
            onValueChange = onPasswordUpdated,
            isError = resetPasswordState.password.isNotEmpty() && !resetPasswordState.isPasswordValid,
            errorMessage = "Enter a valid password",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            enabled = !resetPasswordState.isLoading
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            label = "Confirm Password",
            value = resetPasswordState.confirmPassword,
            onValueChange = onConfirmPasswordUpdated,
            isError = resetPasswordState.confirmPassword.isNotEmpty() && !resetPasswordState.isConfirmPasswordValid,
            errorMessage = "Enter a valid confirm password",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            enabled = !resetPasswordState.isLoading
        )
        Spacer(modifier = Modifier.height(64.dp))
        RedButton(
            label = "Done ",
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(start = 24.dp, end = 24.dp),
            onClick = {
                onResetButtonClicked()
            },
            enabled = resetPasswordState.isResetButtonEnabled,
            isLoading = resetPasswordState.isLoading
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PasswordResetPagePreview(
) {
    PasswordResetPage(
        resetPasswordState = ResetPasswordState(),
        onBackButtonClicked = {},
        onResetButtonClicked = {},
        navigateToResetConfirmationPage = {},
        Resource.success(
            ResetPasswordResponse("")
        ),
        onPasswordUpdated = {},
        onConfirmPasswordUpdated = {}
    )

}