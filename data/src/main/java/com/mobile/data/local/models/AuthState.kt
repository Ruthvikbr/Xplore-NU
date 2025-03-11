package com.mobile.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_state")
data class AuthState (
    @PrimaryKey val id: Int = 1, // Ensure a single row exists
    val isLoggedIn: Boolean
)