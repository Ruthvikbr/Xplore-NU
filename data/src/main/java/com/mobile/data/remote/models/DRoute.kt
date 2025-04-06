package com.mobile.data.remote.models

data class DRoute(
    val weightName: String,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val legs: List<DLeg>,
    val geometry: String
)
