package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DFetchPoiResponse
import com.mobile.data.remote.models.DPointOfInterest
import com.mobile.domain.models.FetchPoiResponse
import com.mobile.domain.models.PointOfInterest

fun DFetchPoiResponse.toFetchPoiResponse() = FetchPoiResponse(count, points.map {
    PointOfInterest(it.id, it.buildingName, it.description, it.lat, it.long, it.ord, it.images)
})

fun FetchPoiResponse.toDFetchPoiResponse() = DFetchPoiResponse(count, points.map {
    DPointOfInterest(it.id, it.buildingName, it.description, it.lat, it.long, it.ord, it.images)
})