package com.mobile.xplore_nu.ui.screens.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.placeholder
import com.mobile.xplore_nu.R
import com.mobile.xplore_nu.ui.components.ImageSlider

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventDetailsPage(
    eventImages: List<String>?,
    eventName: String?,
    eventDate: String?,
    eventLocation: String?,
    eventDescription: String?
) {
    Column {
        ImageSlider(images = eventImages ?: listOf(""))
        eventName?.let {
            Text(
                text = it,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
                    .padding(bottom = 10.dp)
            )
        }
        Row {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "",
                modifier = Modifier.padding(start = 16.dp)
            )
            eventDate?.let {
                Text(
                    text = it,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
        Row {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "",
                modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
            )
            eventLocation?.let {
                Text(
                    text = it,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
            }
        }
        Text(
            text = "About event",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        eventDescription?.let {
            Text(
                text = it,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}