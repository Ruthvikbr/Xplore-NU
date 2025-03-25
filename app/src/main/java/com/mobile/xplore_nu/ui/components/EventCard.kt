package com.mobile.xplore_nu.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.integration.ktx.Status
import com.mobile.xplore_nu.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventCard(
    imageUrl: String?,
    eventName: String,
    date: String,
    location: String
) {
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column {
            if (imageUrl != null) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = " ",
                    loading = placeholder(R.drawable.event_placeholder),
                    failure = placeholder(R.drawable.event_placeholder)
                )
            } else {
                GlideImage(model = R.drawable.event_placeholder, contentDescription = "")
            }
            Text(
                text = eventName,
                color = Color.Black,
                fontSize = TextUnit(20f, TextUnitType.Sp),
                modifier = Modifier
                    .padding(start = 24.dp, top = 10.dp)
            )
            Row {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 24.dp)
                )
                Text(
                    text = date,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Row {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 24.dp, bottom = 10.dp)
                )
                Text(
                    text = location,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
            }
        }
    }
}