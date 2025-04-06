package com.mobile.xplore_nu.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobile.xplore_nu.R

@Composable
fun LeftChevron(onClick : () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
        contentDescription = "back button",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .width(24.dp)
            .height(24.dp)
            .clickable {
                onClick()
            }
    )
}