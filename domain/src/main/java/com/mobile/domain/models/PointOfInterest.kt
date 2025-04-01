package com.mobile.domain.models


data class PointOfInterest(
    val id: String,
    val buildingName: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val ord: Int,
    val images: List<String>,
)

