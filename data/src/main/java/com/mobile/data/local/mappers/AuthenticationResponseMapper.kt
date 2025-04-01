package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DAuthenticationResponse
import com.mobile.domain.models.AuthenticationResponse

fun DAuthenticationResponse.toAuthenticationResponse() = AuthenticationResponse(
    message, token, refreshToken, dUser.toUser()
)

fun AuthenticationResponse.toDAuthenticationResponse() =
    DAuthenticationResponse(message, token, user.toDUser(), refreshToken)