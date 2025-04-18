package com.mobile.xplore_nu.ui.screens.tour

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.mobile.xplore_nu.ui.components.ImageSlider

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BuildingDetailsPage(
    buildingImages: List<String>?,
    buildingName: String?,
    buildingDescription: String?
) {
    Column {
        ImageSlider(images = buildingImages ?: listOf(""))
        buildingName?.let {
            Text(
                text = it,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 10.dp)
            )
        }
        Text(
            text = "About building",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        buildingDescription?.let {
            Text(
                text = it,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}