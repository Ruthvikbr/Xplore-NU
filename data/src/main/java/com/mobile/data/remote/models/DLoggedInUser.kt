package com.mobile.data.remote.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DLoggedInUser(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String
) : Parcelable
