package com.mobile.xplore_nu.ui.screens.tour

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mobile.xplore_nu.ui.components.tour.MapComposable
import com.mobile.xplore_nu.ui.components.tour.PermissionDeniedComposable

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TourPage() {

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    val context = LocalContext.current

    var hasRequestedPermissions by rememberSaveable { mutableStateOf(false) }
    var permissionRequestCompleted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(hasRequestedPermissions) {
        if (hasRequestedPermissions) {
            permissionRequestCompleted = permissionState.revokedPermissions.isNotEmpty()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(if (permissionState.allPermissionsGranted) 0.dp else 24.dp)
    ) {
        when {
            permissionState.allPermissionsGranted -> {
                MapComposable(modifier = Modifier.fillMaxSize())
            }

            permissionState.shouldShowRationale -> {
                PermissionDeniedComposable(onButtonClick = {
                    permissionState.launchMultiplePermissionRequest()
                    hasRequestedPermissions = true
                })
            }

            else -> {
                if (permissionRequestCompleted) {
                    PermissionDeniedComposable(
                        onButtonClick = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            context.startActivity(intent)
                        },
                        messageText = "Location permission denied. Please enable it in the settings to proceed"
                    )
                } else {
                    PermissionDeniedComposable(onButtonClick = {
                        permissionState.launchMultiplePermissionRequest()
                        hasRequestedPermissions = true
                    })
                }
            }
        }
    }
}
