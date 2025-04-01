package com.mobile.domain.models

data class UpcomingEventResponse(
    val success: Boolean,
    val count: Int,
    val events: List<Event>,
)
