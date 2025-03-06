package com.mobile.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DUser(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String
)
