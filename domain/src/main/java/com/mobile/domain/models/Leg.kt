package com.mobile.domain.models

data class Leg(
    val viaWaypoints: List<Any>, // Assuming no specific data type for viaWaypoints
    val admins: List<Admin>,
    val weight: Double,
    val duration: Double,
    val steps: List<Any>, // Assuming no specific data type for steps
    val distance: Double,
    val summary: String
)
