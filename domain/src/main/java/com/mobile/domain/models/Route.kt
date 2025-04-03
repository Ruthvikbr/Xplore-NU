package com.mobile.domain.models

data class Route(
    val weightName: String,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val legs: List<Leg>,
    val geometry: String
)
