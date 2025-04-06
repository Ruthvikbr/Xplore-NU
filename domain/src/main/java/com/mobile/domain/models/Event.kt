package com.mobile.domain.models

import java.util.Date

data class Event(
    val name: String,
    val date: Date,
    val time: String,
    val location: String,
    val description: String,
    val images: List<String>?
)
