package com.mobile.data.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DLoginResponse(
    val message: String,
    val token: String,
    @SerializedName("user")
    val dUser: DLoggedInUser
) : Parcelable

