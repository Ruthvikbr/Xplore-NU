package com.mobile.data.remote.models

import java.util.Date

data class DEvent(
    val name: String,
    val date: Date,
    val time: String,
    val location: String,
    val description: String,
    val images: List<String>?
)
