package com.mobile.domain.models

data class RouteResponse(
    val routes: List<Route>,
    val waypoints: List<Waypoint>,
    val code: String,
    val uuid: String
)
