package com.mobile.data.local.models

import androidx.room.Entity

@Entity
data class DUser(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String
)
