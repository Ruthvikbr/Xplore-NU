package com.mobile.data.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mobile.data.local.models.DUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class DAuthenticationResponse(
    val message: String,
    @SerializedName(value = "accessToken")
    val token: String,
    @SerializedName("user")
    val dUser: DUser,
    val refreshToken: String,
) : Parcelable

