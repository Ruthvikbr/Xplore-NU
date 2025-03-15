package com.mobile.xplore_nu.ui.components

import android.widget.ProgressBar
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RedButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        shape = RoundedCornerShape(20),
        modifier = modifier,
        enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(trackColor = Color.White, strokeCap = StrokeCap.Round, modifier = Modifier.size(24.dp))
        } else {
            Text(text = label, color = Color.White)
        }
    }
}

@Preview
@Composable
fun RadioButtonLoadingPreview() {
    RedButton(
        label = "SUBMIT",
        onClick = {},
        isLoading = true,
        modifier = Modifier
            .width(300.dp)
            .height(54.dp)
    )
}

@Preview
@Composable
fun RadioButtonPreview() {
    RedButton(
        label = "SUBMIT",
        onClick = {},
        isLoading = false,
        modifier = Modifier
            .width(300.dp)
            .height(54.dp)
    )
}