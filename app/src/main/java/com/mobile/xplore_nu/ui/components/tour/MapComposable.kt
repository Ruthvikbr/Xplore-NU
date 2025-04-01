package com.mobile.xplore_nu.ui.components.tour

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.style.BooleanValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.StandardStyleConfigurationState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.mobile.domain.models.PointOfInterest
import com.mobile.xplore_nu.R
import com.mobile.xplore_nu.ui.components.common.RedButton
import com.mobile.xplore_nu.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapComposable(modifier: Modifier, fetchPoints: () -> Unit, points: List<PointOfInterest>) {

    val mapViewportState = rememberMapViewportState {
        // Set the initial camera position
        setCameraOptions {
            center(Point.fromLngLat(0.0, 0.0))
            zoom(0.0)
            pitch(0.0)
        }
    }
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()


    var selectedPoint by remember { mutableStateOf<PointOfInterest?>(null) }

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = { MapboxStandardStyle(
                init = { StandardStyleConfigurationState.let {
                    showPlaceLabels = BooleanValue(false)
                    showRoadLabels = BooleanValue(false)
                    showTransitLabels = BooleanValue(false)
                    showPointOfInterestLabels = BooleanValue(false)
                }}
            )},
            scaleBar = {},
            logo = {},
            attribution = {},
            onMapClickListener = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
                selectedPoint = null
                true
            }
        ) {
            MapEffect(Unit) { mapView ->
                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    puckBearingEnabled = true
                    puckBearing = PuckBearing.HEADING
                    enabled = true
                }
            }

            for (point in points) {
                ViewAnnotation(
                    options = viewAnnotationOptions {
                        geometry(Point.fromLngLat(point.long, point.lat))
                    }
                ) {
                    Marker(point, onMarkerClicked = {
                        selectedPoint = point
                    })
                }
            }
        }

        selectedPoint?.let { point ->
            InfoBottomSheet(
                point = selectedPoint!!,
                bottomSheetState = bottomSheetState,
                onDismiss = { selectedPoint = null }
            )
        }

        RedButton(
            onClick = {
                mapViewportState.transitionToFollowPuckState()
                fetchPoints()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            label = "Start tour"
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    point: PointOfInterest,
    bottomSheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
            Text(text = point.buildingName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = point.description)
            Spacer(modifier = Modifier.height(16.dp))
            RedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                label = "More Details"
            )
        }
    }
}

@Composable
fun Marker(point: PointOfInterest, onMarkerClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clickable(
                onClick = {
                    onMarkerClicked()
                }
            )) {
        Text(point.buildingName, style = Typography.labelSmall)
        Image(
            painter = painterResource(R.drawable.red_marker),
            contentDescription = "Marker",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(16.dp)
        )
    }
}