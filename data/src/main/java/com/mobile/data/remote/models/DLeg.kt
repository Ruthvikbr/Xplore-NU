package com.mobile.data.remote.models

data class DLeg(
    val viaWaypoints: List<Any>, // Assuming no specific data type for viaWaypoints
    val admins: List<DAdmin>,
    val weight: Double,
    val duration: Double,
    val steps: List<Any>, // Assuming no specific data type for steps
    val distance: Double,
    val summary: String
)
