package com.mobile.xplore_nu.ui.components.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.mobile.xplore_nu.ui.theme.fontFamily

@Composable
fun AppNameHeader() {
    Text(buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black)) {
            append("Xplore")
        }
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("NU")
        }
    },
        style = TextStyle(
            fontSize = 36.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight(600)
        )
    )
}