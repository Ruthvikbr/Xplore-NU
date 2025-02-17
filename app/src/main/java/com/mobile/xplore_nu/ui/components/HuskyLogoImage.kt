package com.mobile.xplore_nu.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nbapps.xplore_nu.R

@Composable
fun HuskyLogoImage() {
    Image(
        painter = painterResource(id = R.drawable.husky_logo),
        contentDescription = "image description",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .width(285.dp)
            .height(238.dp)
    )
}