package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DEvent
import com.mobile.data.remote.models.DUpcomingEventsResponse
import com.mobile.domain.models.Event
import com.mobile.domain.models.UpcomingEventResponse

fun UpcomingEventResponse.toDUpcomingEventResponse() = DUpcomingEventsResponse(success, count, events.map {
    DEvent(it.name, it.date, it.time, it.location, it.description, it.images)
})

fun DUpcomingEventsResponse.toUpcomingEventResponse() = UpcomingEventResponse(success, count, events.map {
    Event(it.name, it.date, it.time, it.location, it.description, it.images)
})