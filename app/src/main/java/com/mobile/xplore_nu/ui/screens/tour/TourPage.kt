package com.mobile.xplore_nu.ui.screens.tour

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.mobile.xplore_nu.ui.components.RedButton

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TourPage(
    onButtonClicked: () -> Unit
) {

    val permissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val context = LocalContext.current

    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var permissionRequestCompleted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(hasRequestedPermission) {
        if (hasRequestedPermission) {
            permissionRequestCompleted = true
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when (val status = permissionState.status) {
            is PermissionStatus.Denied -> {
                if (permissionRequestCompleted) {
                    if (status.shouldShowRationale) {
                        Text("Location permission is required to access this feature")
                        RedButton(label = "Enable Location Permission", onClick = {
                            permissionState.launchPermissionRequest()
                            hasRequestedPermission = true
                        })
                    } else {
                        Text("Location permission denied. Please enable it in the settings to proceed")
                        RedButton(label = "Enable Location Permission", onClick = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            context.startActivity(intent)
                        })
                    }
                } else {
                    Text("Location permission is required to access this feature")
                    RedButton(label = "Enable Location Permission", onClick = {
                        permissionState.launchPermissionRequest()
                        hasRequestedPermission = true
                    })
                }
            }

            PermissionStatus.Granted -> {
                MapComposable()
            }
        }
    }
}

@Composable
fun MapComposable() {
    Text("Home screen")
}
