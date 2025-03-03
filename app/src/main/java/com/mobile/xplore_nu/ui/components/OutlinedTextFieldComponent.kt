package com.mobile.xplore_nu.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextFieldComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    isError: Boolean = false,
) {

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}