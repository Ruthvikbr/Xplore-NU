package com.mobile.xplore_nu.ui.screens.event

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.domain.models.Event
import com.mobile.xplore_nu.ui.components.EventCard
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun EventsPage(
    events: List<Event>?,
    navController: NavController
) {
    LaunchedEffect(events) {
        Log.d("EVENTS", events?.toString() ?: "No Events")
    }

    if (events != null) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            item {
                Text(
                    text = "Upcoming Events",
                    modifier = Modifier.padding(top = 24.dp),
                    fontSize = TextUnit(32f, TextUnitType.Sp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
            items(events) {
                val encodedImageUrls = it.images?.joinToString(",") { url ->
                    URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                } ?: ""
                EventCard(
                    imageUrl = it.images?.firstOrNull(),
                    eventName = it.name,
                    date = it.date.toFormattedString(),
                    location = it.location,
                    onCardClicked = {navController.navigate("details/${encodedImageUrls}/${it.name}/${it.date.toFormattedString()}/${it.location}/${it.description}")}
                )
            }
        }
    }
}


fun Date.toFormattedString() : String {
    return SimpleDateFormat("MMMM dd, yyyy").format(this)
}