package com.mobile.domain.models

data class FetchPoiResponse(
    val count: Int,
    val points: List<PointOfInterest>
)
