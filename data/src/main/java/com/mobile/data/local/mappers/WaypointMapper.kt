package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DWaypoint
import com.mobile.domain.models.Waypoint

fun DWaypoint.toWaypoint() = Waypoint(distance, name, location)

fun Waypoint.toDWayPoint() = DWaypoint(distance, name, location)