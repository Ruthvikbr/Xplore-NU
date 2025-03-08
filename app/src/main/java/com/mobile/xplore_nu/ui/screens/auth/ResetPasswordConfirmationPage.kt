package com.mobile.xplore_nu.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.xplore_nu.ui.components.AppNameHeader
import com.mobile.xplore_nu.ui.components.HuskyLogoImage
import com.mobile.xplore_nu.ui.components.LeftChevron
import com.mobile.xplore_nu.ui.components.RedButton
import com.mobile.xplore_nu.ui.theme.fontFamily

@Composable
fun ResetPasswordConfirmationPage(
    onBackToLoginButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f, false)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LeftChevron(onClick = onBackButtonClicked)
            }
            Spacer(modifier = Modifier.height(64.dp))
            AppNameHeader()
            HuskyLogoImage()
            Text(
                text = "Password reset \n successfully!",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000)
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        RedButton(
            label = "Get OTP", modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(start = 24.dp, end = 24.dp),
            onClick = onBackToLoginButtonClicked,
//            enabled = forgotPasswordState.canRequestOtp,
            enabled = true,
            isLoading = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordConfirmationPagePreview() {
    ResetPasswordConfirmationPage(
        onBackButtonClicked = {},
        onBackToLoginButtonClicked = {})
}