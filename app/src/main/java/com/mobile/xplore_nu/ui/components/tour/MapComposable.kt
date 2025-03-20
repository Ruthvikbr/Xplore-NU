package com.mobile.xplore_nu.ui.components.tour

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle

@Composable
fun MapComposable(modifier: Modifier) {
    val mapViewportState = rememberMapViewportState {
        // Set the initial camera position
        setCameraOptions {
            center(Point.fromLngLat(0.0, 0.0))
            zoom(0.0)
            pitch(0.0)
        }
    }

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = {
                MapStyle(style = Style.STANDARD)
            }
        )
    }

}