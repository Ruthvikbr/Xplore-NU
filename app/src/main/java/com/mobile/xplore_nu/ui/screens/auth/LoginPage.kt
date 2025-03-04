package com.mobile.xplore_nu.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.xplore_nu.ui.components.AppNameHeader
import com.mobile.xplore_nu.ui.components.HuskyLogoImage
import com.mobile.xplore_nu.ui.components.OutlinedTextFieldComponent
import com.mobile.xplore_nu.ui.components.RedButton
import com.mobile.xplore_nu.ui.theme.fontFamily

@Composable
fun LoginPage(onRegisterButtonClicked : () -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember {
        mutableStateOf("")
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
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            label = "Email ID",
            value = email.value,
            onValueChange = { value ->
                email.value = value
            },
            isError = false,
            errorMessage = "Enter a valid email ID"
        )
        OutlinedTextFieldComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            label = "Password",
            value = password.value,
            onValueChange = { value ->
                password.value = value
            },
            isError = false,
            errorMessage = "Enter a valid password"
        )
        TextButton(onClick = { /*TODO*/ }) {
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
        RedButton(label = "Login", onClick = {}, enabled = false)
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