package com.mobile.data.remote.models

import com.google.gson.annotations.SerializedName

data class DPointOfInterest(
    @SerializedName(value = "_id")
    val id: String,
    @SerializedName(value = "building_name")
    val buildingName: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val ord: Int,
    val images: List<String>,
)
