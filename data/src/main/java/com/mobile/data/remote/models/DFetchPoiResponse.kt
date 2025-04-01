package com.mobile.data.remote.models

import com.google.gson.annotations.SerializedName

data class DFetchPoiResponse(
    val count: Int,
    @SerializedName(value = "pois")
    val points: List<DPointOfInterest>
)