package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DRouteResponse
import com.mobile.domain.models.RouteResponse

fun DRouteResponse.toRouteResponse() = RouteResponse(routes.map { it.toRoute() }, waypoints.map { it.toWaypoint() }, code, uuid)

fun RouteResponse.toDRouteResponse() = DRouteResponse(routes.map { it.toDRoute() }, waypoints.map { it.toDWayPoint() }, code, uuid)