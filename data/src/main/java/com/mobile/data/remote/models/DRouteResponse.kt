package com.mobile.data.remote.models

data class DRouteResponse (
    val routes: List<DRoute>,
    val waypoints: List<DWaypoint>,
    val code: String,
    val uuid: String
)