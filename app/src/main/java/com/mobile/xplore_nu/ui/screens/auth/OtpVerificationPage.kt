package com.mobile.xplore_nu.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.libfactory.otpverificationcompose.OtpComponent
import com.libfactory.otpverificationcompose.OtpConfig
import com.mobile.xplore_nu.ui.components.AppNameHeader
import com.mobile.xplore_nu.ui.components.HuskyLogoImage
import com.mobile.xplore_nu.ui.components.LeftChevron
import com.mobile.xplore_nu.ui.components.RedButton
import com.mobile.xplore_nu.ui.theme.fontFamily

@Composable
fun OtpVerificationPage(
    onBackButtonClicked: () -> Unit,
    onVerifyOtpButtonClicked: () -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    var isOtpFilled by remember { mutableStateOf(false) }

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
            text = "Enter OTP",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFF000000)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OtpComponent(
            modifier = Modifier.fillMaxWidth(),
            config = otpConfig,
            onOtpModified = { value, otpFilled ->
                otpValue = value
                isOtpFilled = otpFilled
                if (otpFilled) {
                    // call API to verify OTP?
                }
            },
            onResendClicked = {
                // call API to resend OTP?
                Log.d("Resend", "Resend clicked")
            },
            validation = {
                // add your custom validation logic here
                it == "123456" // if OTP is anything other than 123456, it will show error message
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        RedButton(
            label = "Verify", modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(start = 24.dp, end = 24.dp),
            onClick = onVerifyOtpButtonClicked,
            enabled = true,
            isLoading = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun OtpVerificationPagePreview() {
    OtpVerificationPage(
        onBackButtonClicked = {},
        onVerifyOtpButtonClicked = {})
}

val otpConfig = OtpConfig(
    boxCount = 6,
    boxSize = 56.dp,
    boxSpacing = 12.dp,
    boxShape = RoundedCornerShape(8.dp),
    focusedColor = Color.Black,
    unfocusedColor = Color.LightGray,
    errorColor = Color.Red,
    textColor = Color.Black,
    backgroundColor = Color.White,
    textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
    shouldShowCursor = true,
    shouldCursorBlink = true,
    errorMessage = "Invalid OTP",
    timerSeconds = 60L,
    timerTextStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
)