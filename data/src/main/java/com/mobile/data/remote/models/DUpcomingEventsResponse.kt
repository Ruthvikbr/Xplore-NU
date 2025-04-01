package com.mobile.data.remote.models

data class DUpcomingEventsResponse(
    val success: Boolean,
    val count: Int,
    val events: List<DEvent>,
)
