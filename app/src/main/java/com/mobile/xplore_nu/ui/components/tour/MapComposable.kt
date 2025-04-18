package com.mobile.xplore_nu.ui.components.tour

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.style.BooleanValue
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.StandardStyleConfigurationState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.options.NavigationOptions.Builder
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.trip.model.RouteLegProgress
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.arrival.ArrivalObserver
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.trip.session.TripSessionState
import com.mapbox.navigation.core.trip.session.TripSessionStateObserver
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mobile.domain.models.PointOfInterest
import com.mobile.xplore_nu.R
import com.mobile.xplore_nu.ui.components.common.RedButton
import com.mobile.xplore_nu.ui.theme.Typography
import com.mobile.xplore_nu.ui.uistates.TourUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPreviewMapboxNavigationAPI::class)
@Composable
fun MapComposable(
    modifier: Modifier,
    points: List<PointOfInterest>,
    startTour: (point: Point) -> Unit,
    mapUiState: TourUiState,
    onEvent: (TourUiState) -> Unit,
    onMoreDetailsRequested: (PointOfInterest) -> Unit
) {
    val context = LocalContext.current
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
    var locationComponentPlugin: LocationComponentPlugin? by remember { mutableStateOf(null) }
    var mapboxNavigation by remember { mutableStateOf<MapboxNavigation?>(null) }
    var routesRequestCallback by remember { mutableStateOf<NavigationRouterCallback?>(null) }
    var tripSessionState by remember { mutableStateOf<TripSessionState?>(null) }
    var mapView: MapView? by remember { mutableStateOf(null) }
    var viewportDataSource by remember { mutableStateOf<MapboxNavigationViewportDataSource?>(null) }
    var navigationCamera by remember { mutableStateOf<NavigationCamera?>(null) }
    val scope = rememberCoroutineScope()
    var currentLocation by remember { mutableStateOf<Point?>(null) }

    var arrived by remember { mutableStateOf(false) }

    val routesObserver = remember {
        object : RoutesObserver {
            override fun onRoutesChanged(result: RoutesUpdatedResult) {

                mapView?.let { mv ->
                    displayRouteOnMap(
                        mv,
                        result.navigationRoutes.map { it.directionsRoute }
                    )
                }
                viewportDataSource?.onRouteChanged(result.navigationRoutes[0])
            }
        }
    }

    val tripSessionStateObserver = remember {
        object : TripSessionStateObserver {
            override fun onSessionStateChanged(mTripSessionState: TripSessionState) {
                tripSessionState = mTripSessionState
            }

        }
    }

    val arrivalObserver = remember {
        object : ArrivalObserver {
            override fun onFinalDestinationArrival(routeProgress: RouteProgress) {
                arrived = true
            }

            override fun onNextRouteLegStart(routeLegProgress: RouteLegProgress) {
            }

            override fun onWaypointArrival(routeProgress: RouteProgress) {
                arrived = true
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, mapboxNavigation, arrivalObserver) {
        val lifecycleObserver = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapboxNavigation?.registerRoutesObserver(routesObserver)
                    mapboxNavigation?.registerArrivalObserver(arrivalObserver)
                    mapboxNavigation?.registerTripSessionStateObserver(tripSessionStateObserver)
                }

                Lifecycle.Event.ON_STOP -> {
                    mapboxNavigation?.unregisterRoutesObserver(routesObserver)
                    mapboxNavigation?.unregisterArrivalObserver(arrivalObserver)
                    mapboxNavigation?.unregisterTripSessionStateObserver(tripSessionStateObserver)
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            mapboxNavigation?.unregisterRoutesObserver(routesObserver)
            mapboxNavigation?.unregisterArrivalObserver(arrivalObserver)
            mapboxNavigation?.unregisterTripSessionStateObserver(tripSessionStateObserver)
        }
    }

    LaunchedEffect(Unit) {
        mapboxNavigation =
            MapboxNavigationProvider.create(Builder(context).build())

        if (!MapboxNavigationApp.isSetup()) {
            MapboxNavigationApp.setup {
                Builder(context)
                    .build()
            }
        }
        routesRequestCallback = object : NavigationRouterCallback {
            override fun onRoutesReady(
                routes: List<NavigationRoute>,
                routerOrigin: String
            ) {
                mapboxNavigation!!.setNavigationRoutes(routes)
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (granted) {
                    mapboxNavigation?.startTripSession(withForegroundService = true)
                }

            }

            override fun onFailure(
                reasons: List<RouterFailure>,
                routeOptions: RouteOptions
            ) {
            }

            override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {
            }
        }
    }

    LaunchedEffect(mapUiState, arrived) {
        when (mapUiState) {
            is TourUiState.StartTour -> {

            }

            is TourUiState.HeadingToStartLocation -> {
                if (arrived) {
                    onEvent(
                        TourUiState.DisplayStartTourInfo(
                            mapUiState.currentLong,
                            mapUiState.currentLat
                        )
                    )

                }
            }

            is TourUiState.NavigatingToNextBuilding -> {
                if (arrived) {
                    if (mapUiState.nextBuilding == points.last()) {
                        onEvent(
                            TourUiState.DisplayBuildingInfo(
                                mapUiState.nextBuilding,
                                mapUiState.nextBuilding
                            )
                        )
                    } else {
                        val currentBuilding = mapUiState.nextBuilding
                        val newNextBuilding = points[points.indexOf(mapUiState.nextBuilding) + 1]
                        onEvent(TourUiState.DisplayBuildingInfo(currentBuilding!!, newNextBuilding))
                    }
                }
            }

            else -> {}
        }
    }

    var selectedPoint by remember { mutableStateOf<PointOfInterest?>(null) }

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = {
                MapboxStandardStyle(
                    init = {
                        StandardStyleConfigurationState.let {
                            showPlaceLabels = BooleanValue(false)
                            showRoadLabels = BooleanValue(false)
                            showTransitLabels = BooleanValue(false)
                            showPointOfInterestLabels = BooleanValue(false)
                            lightPreset = LightPresetValue.DUSK
                        }
                    },

                    )
            },
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
            MapEffect(Unit) { mv ->
                mapView = mv
                viewportDataSource = MapboxNavigationViewportDataSource(mv.mapboxMap)
                navigationCamera = NavigationCamera(mv.mapboxMap, mv.camera, viewportDataSource!!)
                mapViewportState.transitionToFollowPuckState()

                locationComponentPlugin = mv.location.apply {
                    updateSettings {
                        locationPuck = createDefault2DPuck(withBearing = true)
                        puckBearingEnabled = true
                        puckBearing = PuckBearing.HEADING
                        enabled = true
                    }
                }
            }

            LaunchedEffect(locationComponentPlugin) {
                locationComponentPlugin?.addOnIndicatorPositionChangedListener { point ->
                    currentLocation = point
                    viewportDataSource?.onLocationChanged(
                        Location.Builder().latitude(point.latitude()).longitude(point.longitude())
                            .build()
                    )

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
                title = selectedPoint!!.buildingName,
                description = selectedPoint!!.description,
                bottomSheetState = bottomSheetState,
                onDismiss = {
                    selectedPoint = null
                },
                buttonLabel = "More Details",
                onButtonClicked = {
                    onMoreDetailsRequested(selectedPoint!!)
                }
            )
        }

        if (mapUiState is TourUiState.DisplayStartTourInfo) {
            InfoBottomSheet(
                title = "Welcome to Northeastern",
                description = "You’ve arrived at the perfect spot to begin your self-guided tour. Dive into the rich history, explore hidden gems, and discover fascinating stories as you move at your own pace. Tap “Start” whenever you’re ready to begin exploring!",
                bottomSheetState = bottomSheetState,
                onDismiss = {
                    scope.launch {
                        bottomSheetState.hide()
                        onEvent(TourUiState.DisplayBuildingInfo(points[0], points[1]))
                    }
                },
                buttonLabel = "Get Started",
                onButtonClicked = {
                    scope.launch {
                        bottomSheetState.hide()
                        onEvent(TourUiState.DisplayBuildingInfo(points[0], points[1]))
                    }
                }
            )
        } else if (mapUiState == TourUiState.TourCompleted) {
            InfoBottomSheet(
                title = "Thank you!",
                description = "You've reached the end of the self-guided tour. We hope you enjoyed exploring Northeastern!",
                bottomSheetState = bottomSheetState,
                onDismiss = {
                    scope.launch {
                        bottomSheetState.hide()
                        onEvent(
                            TourUiState.StartTour
                        )
                    }
                },
                buttonLabel = "Request Callback",
                onButtonClicked = {
                    scope.launch {
                        bottomSheetState.hide()
                        onEvent(TourUiState.DisplayBuildingInfo(points[0], points[1]))
                    }
                }
            )
        } else if (mapUiState is TourUiState.DisplayBuildingInfo) {
            InfoBottomSheet(
                title = mapUiState.currentBuilding.buildingName,
                description = mapUiState.currentBuilding.description,
                bottomSheetState = bottomSheetState,
                onDismiss = {
                    scope.launch {
                        if (mapUiState.nextBuilding == mapUiState.currentBuilding) {
                            onEvent(TourUiState.TourCompleted)
                        } else {
                            mapboxNavigation?.requestRoutes(
                                RouteOptions.builder()
                                    .applyDefaultNavigationOptions()
                                    .coordinatesList(
                                        listOf(
                                            Point.fromLngLat(
                                                mapUiState.currentBuilding.long,
                                                mapUiState.currentBuilding.lat
                                            ),
                                            Point.fromLngLat(
                                                mapUiState.nextBuilding.long,
                                                mapUiState.nextBuilding.lat
                                            )
                                        )
                                    )
                                    .profile(DirectionsCriteria.PROFILE_WALKING)
                                    .build(),
                                routesRequestCallback!!
                            )
                            onEvent(
                                TourUiState.NavigatingToNextBuilding(
                                    currentBuilding = mapUiState.currentBuilding,
                                    nextBuilding = mapUiState.nextBuilding
                                )
                            )
                        }
                        arrived = false
                        bottomSheetState.hide()
                    }
                },
                buttonLabel = "More Details",
                onButtonClicked = {
                    onMoreDetailsRequested(mapUiState.currentBuilding)
                }
            )
        }

        RedButton(
            onClick = {
                when (mapUiState) {
                    TourUiState.TourCompleted -> {
                        mapboxNavigation?.stopTripSession()
                    }

                    is TourUiState.DisplayBuildingInfo -> {

                    }

                    TourUiState.StartTour -> {
                        currentLocation?.let {
                            startTour(it)
                        }
                        mapboxNavigation?.requestRoutes(
                            RouteOptions.builder()
                                .applyDefaultNavigationOptions()
                                .coordinatesList(
                                    listOf<Point>(
                                        Point.fromLngLat(
                                            currentLocation!!.longitude(),
                                            currentLocation!!.latitude()
                                        ),
                                        Point.fromLngLat(points[0].long, points[0].lat)
                                    )
                                )
                                .profile(DirectionsCriteria.PROFILE_WALKING)
                                .build(),
                            routesRequestCallback!!
                        )

                    }

                    else -> {

                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            label = when (mapUiState) {

                TourUiState.StartTour -> "Start Tour"
                is TourUiState.HeadingToStartLocation -> "Head to start location"
                is TourUiState.DisplayBuildingInfo -> "Resume Tour"
                is TourUiState.NavigatingToNextBuilding -> {
                    if (mapUiState.nextBuilding == points.last() || mapUiState.nextBuilding == null) {
                        "Heading to last location"
                    } else {
                        "Heading to next location"
                    }
                }

                TourUiState.TourCompleted -> "End Tour"
                else -> ""

            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    title: String,
    description: String,
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    buttonLabel: String,
    onButtonClicked: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
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
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description)
            Spacer(modifier = Modifier.height(16.dp))
            RedButton(
                onClick = onButtonClicked,
                modifier = Modifier.fillMaxWidth(),
                label = buttonLabel
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
        Text(point.buildingName, style = Typography.labelSmall, color = Color.White)
        Image(
            painter = painterResource(R.drawable.red_marker),
            contentDescription = "Marker",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(16.dp)
        )
    }
}


fun displayRouteOnMap(mapView: MapView, routes: List<DirectionsRoute>) {
    if (routes.isNotEmpty()) {
        val route = routes[0]
        val coordinates =
            route.geometry()?.let { com.mapbox.geojson.utils.PolylineUtils.decode(it, 6) }
        coordinates?.let {

            val polylineAnnotationOptions = PolylineAnnotationOptions()
                .withPoints(it)
                .withLineColor("#ff0000")
                .withLineWidth(5.0)

            mapView.annotations.createPolylineAnnotationManager().create(polylineAnnotationOptions)
        }
    }
}