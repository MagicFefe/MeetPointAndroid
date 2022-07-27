package com.swaptech.meet.presentation.screen.home.meetpoint

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.swaptech.meet.domain.meet.CreateMeetPoint
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.presentation.CITY_LEVEL_ZOOM
import com.swaptech.meet.presentation.utils.MeetPointCreationCard
import com.swaptech.meet.presentation.utils.MeetPointDetails
import com.swaptech.meet.presentation.utils.MeetPointMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MeetPointScreen(
    localUser: UserResponse,
    viewModel: MeetPointScreenViewModel
) {
    Box {
        val permissionsState = rememberMultiplePermissionsState(
            permissions = permissions
        )
        LaunchedEffect(key1 = !permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
        if (permissionsState.allPermissionsGranted) {
            val firstLocationFixHappened = viewModel.firstLocationFixHappened
            val scope = rememberCoroutineScope()
            val meetPoints by viewModel.meetPoints.collectAsState(listOf())
            val oldMeetPoints by viewModel.cachedMeetPoints.collectAsState(listOf())
            val screenState = viewModel.screenState
            val clickedMeetPoint = viewModel.clickedMeetPoint
            MeetPointMap(
                centerLatitude = viewModel.savedMapPosition.latitude,
                centerLongitude = viewModel.savedMapPosition.longitude,
                zoomLevel = viewModel.savedMapZoomLevel,
                newMeetPoints = meetPoints,
                oldMeetPoints = oldMeetPoints,
                onFirstLocationFix = { locationOverlay, map ->
                    if (!firstLocationFixHappened) {
                        locationOverlay.runOnFirstFix {
                            scope.launch(Dispatchers.Main) {
                                map.controller.animateTo(
                                    locationOverlay.myLocation,
                                    CITY_LEVEL_ZOOM,
                                    ANIMATION_DURATION_MS
                                )
                                map.controller.setCenter(locationOverlay.myLocation)
                            }
                        }
                        viewModel.onFirstFixLocation()
                    }
                },
                onMapSingleTap = { geoPoint, map ->
                    geoPoint?.let {
                        val newMeetPointMarker = Marker(map).apply {
                            setOnMarkerClickListener { _, _ ->
                                false
                            }
                            position = it
                        }
                        viewModel.meetPointMarker?.let { oldMeetPointMarker ->
                            map.overlays.remove(oldMeetPointMarker)
                        }
                        map.overlays.add(newMeetPointMarker)
                        map.controller.animateTo(it)
                        viewModel.addMeetPointMarker(newMeetPointMarker, map)
                    }
                    true
                },
                onMarkerClickListener = { marker, map ->
                    val clickedMeetPointLocal =
                        meetPoints.first { GeoPoint(it.latitude, it.longitude) == marker.position }
                    map.controller.animateTo(marker.position)
                    viewModel.getMeetPointById(clickedMeetPointLocal.id)
                    viewModel.showMeetPointDetails()
                    true
                },
                onDisposeMap = { map ->
                    viewModel.saveMapPosition(map.mapCenter)
                    viewModel.saveMapZoomLevel(map.zoomLevelDouble)
                }
            )
            AnimatedVisibility(
                visible = screenState !is MeetPointScreenState.Idle,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically { fullHeight -> fullHeight },
                exit = slideOutVertically { fullHeight -> fullHeight }
            ) {
                when (screenState) {
                    is MeetPointScreenState.CreateMeetPoint -> {
                        val (meetPointName, onMeetPointNameChange) = rememberSaveable {
                            mutableStateOf("")
                        }
                        val (meetPointDescription, onMeetPointDescription) = rememberSaveable {
                            mutableStateOf("")
                        }
                        MeetPointCreationCard(
                            meetPointName = meetPointName,
                            onMeetPointNameChange = onMeetPointNameChange,
                            meetPointDescription = meetPointDescription,
                            onMeetPointDescriptionChange = onMeetPointDescription,
                            onCloseButtonCLick = {
                                screenState.map.overlays.remove(viewModel.meetPointMarker)
                                viewModel.removeMeetPointMarker()
                            },
                            onDoneButtonClick = {
                                viewModel.meetPointMarker?.let { marker ->
                                    val newMeetPoint = CreateMeetPoint(
                                        meetName = meetPointName,
                                        meetDescription = meetPointDescription,
                                        authorId = localUser.id,
                                        authorName = localUser.name,
                                        authorSurname = localUser.surname,
                                        latitude = marker.position.latitude,
                                        longitude = marker.position.longitude
                                    )
                                    viewModel.createMeetPoint(newMeetPoint)
                                }
                                screenState.map.overlays.remove(viewModel.meetPointMarker)
                                viewModel.removeMeetPointMarker()
                            }
                        )
                    }
                    is MeetPointScreenState.ShowMeetPointDetails -> {
                        clickedMeetPoint?.let {
                            if (localUser.id == clickedMeetPoint.authorId) {
                                MeetPointDetails(
                                    meetPoint = clickedMeetPoint,
                                    onCloseButtonCLick = {
                                        viewModel.hideMeetPointDetails()
                                    },
                                    actionButton = {
                                        IconButton(
                                            onClick = {
                                                viewModel.deleteMeetPoint(clickedMeetPoint.id)
                                                viewModel.hideMeetPointDetails()
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )
                            } else {
                                MeetPointDetails(
                                    meetPoint = clickedMeetPoint,
                                    onCloseButtonCLick = {
                                        viewModel.hideMeetPointDetails()
                                    }
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

private val permissions = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
)
const val ANIMATION_DURATION_MS = 1000L