package com.swaptech.meet.presentation.utils.map

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.swaptech.meet.R
import com.swaptech.meet.domain.meet.model.MeetPointResponse
import com.swaptech.meet.presentation.MAPNIK_512
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystemWebMercator
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MeetPointMap(
    mapState: MeetPointMapState,
    newMeetPoints: List<MeetPointResponse>,
    oldMeetPoints: List<MeetPointResponse>,
    updateLocation: Boolean,
    localUserId: String,
    onUpdateLocation: (MapView, MyLocationNewOverlay) -> Unit,
    onMapSingleTap: (GeoPoint?, MapView) -> Boolean,
    onMarkerClickListener: (Marker, MapView) -> Boolean,
    onSaveMapState: (MapView) -> Unit,
    mapStateRestored: Boolean,
    onRestoreMapState: (MapView) -> Unit
) {
    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val map = remember(
        mapState.latitude,
        mapState.longitude,
        mapState.mapCenterOffsetX,
        mapState.mapCenterOffsetY,
        mapState.zoomLevel
    ) {
        MapView(context)
    }
    val observer = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    map.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    map.onPause()
                    onSaveMapState(map)
                }
                else -> {}
            }
        }
    }
    LaunchedEffect(Unit) {
        owner.lifecycle.addObserver(observer)
    }
    val mapEventsReceiver = object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean =
            onMapSingleTap(p, map)

        override fun longPressHelper(p: GeoPoint?): Boolean = false
    }
    val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
    val compassOverlay = CompassOverlay(context, InternalCompassOrientationProvider(context), map)
    val rotationGestureOverlay = RotationGestureOverlay(map)
    val scaleBarOverlay = ScaleBarOverlay(map)
    val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map)
    AndroidView(
        factory = {
            map.apply {
                setTileSource(MAPNIK_512)

                zoomController.onDetach()

                isHorizontalMapRepetitionEnabled = false
                isVerticalMapRepetitionEnabled = false
                setScrollableAreaLimitLatitude(
                    MapView.getTileSystem().maxLatitude,
                    MapView.getTileSystem().minLatitude,
                    0
                )
                setScrollableAreaLimitDouble(
                    BoundingBox(
                        TileSystemWebMercator.MaxLatitude,
                        TileSystemWebMercator.MaxLongitude,
                        TileSystemWebMercator.MinLatitude,
                        TileSystemWebMercator.MinLongitude
                    )
                )

                overlays.add(mapEventsOverlay)
                overlays.add(scaleBarOverlay)

                setMultiTouchControls(true)
                if (rotationGestureOverlay.isEnabled) {
                    overlays.add(rotationGestureOverlay)
                }

                if (compassOverlay.enableCompass()) {
                    overlays.add(compassOverlay)
                }

                controller.setCenter(
                    GeoPoint(
                        mapState.latitude,
                        mapState.longitude
                    )
                )
                controller.setZoom(mapState.zoomLevel)
                setMapCenterOffset(mapState.mapCenterOffsetX, mapState.mapCenterOffsetY)

                overlays.add(locationOverlay)
            }
        },
        update = {
            it.overlays.removeAll { overlay ->
                (overlay as? Marker)?.position?.let { geoPoint ->
                    oldMeetPoints.map { oldMeetPoint ->
                        GeoPoint(
                            oldMeetPoint.latitude,
                            oldMeetPoint.longitude
                        )
                    }.contains(geoPoint)
                } ?: false
            }
            newMeetPoints.forEach { meetPointResponse ->
                val meet =
                    Marker(it).apply {
                        setOnMarkerClickListener(onMarkerClickListener)
                        title = meetPointResponse.id
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_circle_24)
                            ?.apply {
                                val meetPointColor =
                                    if (localUserId == meetPointResponse.authorId) {
                                        Color.GREEN
                                    } else {
                                        Color.BLUE
                                    }
                                colorFilter =
                                    BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                                        meetPointColor, BlendModeCompat.SRC_ATOP
                                    )
                            }
                        position = GeoPoint(
                            meetPointResponse.latitude,
                            meetPointResponse.longitude
                        )
                    }
                it.overlays.add(meet)
            }
            if (updateLocation) {
                onUpdateLocation(it, locationOverlay)
            }
            if (!mapStateRestored) {
                onRestoreMapState(it)
            }
        }
    )
    DisposableEffect(Unit) {
        onDispose {
            owner.lifecycle.removeObserver(observer)
            onSaveMapState(map)
        }
    }
}

