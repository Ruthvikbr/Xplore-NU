package com.mobile.xplore_nu.ui.components.tour

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobile.xplore_nu.R
import com.mobile.xplore_nu.ui.components.common.RedButton

@Composable
fun PermissionDeniedComposable(
    onButtonClick: () -> Unit,
    buttonText: String = "Enable Location Permission",
    messageText: String = "Location permission is required to access this feature",
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.permission_denied),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(285.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(messageText)
        RedButton(
            label = buttonText,
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        )
    }
}

@Preview
@Composable
fun PermissionDeniedComposablePreview() {
    PermissionDeniedComposable(onButtonClick = {})
}