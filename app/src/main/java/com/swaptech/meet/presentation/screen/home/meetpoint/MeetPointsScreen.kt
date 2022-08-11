package com.swaptech.meet.presentation.screen.home.meetpoint

import android.Manifest
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.swaptech.meet.R
import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.DeleteMeetPoint
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.presentation.CITY_LEVEL_ZOOM
import com.swaptech.meet.presentation.MAX_MEET_POINT_DESCRIPTION_LENGTH
import com.swaptech.meet.presentation.MAX_MEET_POINT_NAME_LENGTH
import com.swaptech.meet.presentation.navigation.destination.Root
import com.swaptech.meet.presentation.utils.CardProgressIndicator
import com.swaptech.meet.presentation.utils.MeetPointCreationUpdateCard
import com.swaptech.meet.presentation.utils.MeetPointDetails
import com.swaptech.meet.presentation.utils.MeetPointMap
import com.swaptech.meet.presentation.utils.network_error_handling.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MeetPointsScreen(
    localUser: UserResponse,
    viewModel: MeetPointScreenViewModel,
    nestedNavController: NavHostController
) {
    Box {
        val permissionsState = rememberMultiplePermissionsState(
            permissions = permissions
        )
        LaunchedEffect(!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
        if (permissionsState.allPermissionsGranted) {
            val firstLocationFixHappened = viewModel.firstLocationFixHappened
            val scope = rememberCoroutineScope()
            val meetPoints by viewModel.meetPoints.collectAsState(listOf())
            val oldMeetPoints by viewModel.cachedMeetPoints.collectAsState(listOf())
            val screenState = viewModel.screenState
            val clickedMeetPoint = viewModel.clickedMeetPoint
            val context = LocalContext.current
            val focusManager = LocalFocusManager.current
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
                    viewModel.showMeetPointDetails(clickedMeetPointLocal.id)
                    true
                },
                onDisposeMap = { map ->
                    viewModel.saveMapPosition(map.mapCenter)
                    viewModel.saveMapZoomLevel(map.zoomLevelDouble)
                }
            )
            AnimatedMeetPointCard(
                visible = screenState is MeetPointScreenState.CreateMeetPoint,
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                var meetPointName by rememberSaveable {
                    mutableStateOf("")
                }
                var meetPointDescription by rememberSaveable {
                    mutableStateOf("")
                }
                val state by lazy {
                    screenState as MeetPointScreenState.CreateMeetPoint
                }
                MeetPointCreationUpdateCard(
                    cardTitle = stringResource(R.string.create_meet_point),
                    meetPointName = meetPointName,
                    onMeetPointNameChange = { input ->
                        if (meetPointName.length < MAX_MEET_POINT_NAME_LENGTH) {
                            meetPointName = input
                        }
                    },
                    meetPointDescription = meetPointDescription,
                    onMeetPointDescriptionChange = { input ->
                        if (meetPointDescription.length < MAX_MEET_POINT_DESCRIPTION_LENGTH) {
                            meetPointDescription = input
                        }
                    },
                    onCloseButtonCLick = {
                        state.map.overlays.remove(
                            viewModel.meetPointMarker
                        )
                        viewModel.removeMeetPointMarker()
                    },
                    doneButton = if (viewModel.meetPointCreatingInProgress) {
                        { CardProgressIndicator() }
                    } else {
                        null
                    },
                    onDoneButtonClick = {
                        val dataIsValid = meetPointDataIsValid(
                            meetPointName = meetPointName,
                            meetPointDescription = meetPointDescription,
                            onInvalidData = { resourceId ->
                                Toast.makeText(
                                    context,
                                    resourceId,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        if (!dataIsValid) {
                            return@MeetPointCreationUpdateCard
                        }
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
                            viewModel.createMeetPoint(
                                createMeetPoint = newMeetPoint,
                                onFail = { code, message ->
                                    val toastMessage = if (code == 409) {
                                        context.getString(R.string.meet_point_already_exists)
                                    } else {
                                        message
                                    }
                                    Toast.makeText(
                                        context,
                                        toastMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                onError = { error ->
                                    handleError(
                                        error,
                                        onConnectionFault = {
                                            Toast.makeText(
                                                context,
                                                R.string.no_internet_connection,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onSocketTimeout = {
                                            Toast.makeText(
                                                context,
                                                R.string.remote_services_unavailable,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            )
                        }
                        focusManager.clearFocus()
                        state.map.overlays.remove(viewModel.meetPointMarker)
                    }
                )
            }
            AnimatedMeetPointCard(
                visible = screenState is MeetPointScreenState.ShowMeetPointDetails,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                LaunchedEffect(screenState is MeetPointScreenState.ShowMeetPointDetails) {
                    if (screenState is MeetPointScreenState.ShowMeetPointDetails) {
                        viewModel.getMeetPointById(
                            screenState.meetPointId,
                            onFail = { _, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                viewModel.hideMeetPointCard()
                            },
                            onError = { error ->
                                handleError(
                                    error,
                                    onConnectionFault = {
                                        Toast.makeText(
                                            context,
                                            R.string.no_internet_connection,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onSocketTimeout = {
                                        Toast.makeText(
                                            context,
                                            R.string.remote_services_unavailable,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                                viewModel.hideMeetPointCard()
                            }
                        )
                    }
                }
                MeetPointDetails(
                    meetPoint = clickedMeetPoint,
                    isLoading = viewModel.meetPointByIdReceivingInProgress,
                    onCloseButtonCLick = {
                        viewModel.hideMeetPointCard()
                    },
                    onAuthorClick = {
                        clickedMeetPoint?.let {
                            nestedNavController.navigate(
                                Root.UserScreen.getNavigationRoute(
                                    clickedMeetPoint.authorId
                                )
                            ) {
                                launchSingleTop = true
                            }
                        }
                    },
                    actionButtons = if (
                        localUser.id == (clickedMeetPoint?.authorId ?: false)
                    ) {
                        {
                            IconButton(
                                onClick = {
                                    viewModel.showUpdateMeetPointCard()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            }
                            if (viewModel.meetPointByIdDeletingInProgress) {
                                CardProgressIndicator(
                                    modifier = Modifier.padding(end = 13.dp)
                                )
                            } else {
                                IconButton(
                                    onClick = {
                                        clickedMeetPoint?.let {
                                            val deleteMeetPoint = DeleteMeetPoint(
                                                id = clickedMeetPoint.id,
                                                authorId = clickedMeetPoint.authorId
                                            )
                                            viewModel.deleteMeetPoint(
                                                deleteMeetPoint,
                                                onSuccess = {
                                                    viewModel.hideMeetPointCard()
                                                },
                                                onFail = { _, message ->
                                                    Toast.makeText(
                                                        context,
                                                        message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                onError = { error ->
                                                    handleError(
                                                        error,
                                                        onConnectionFault = {
                                                            Toast.makeText(
                                                                context,
                                                                R.string.no_internet_connection,
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        },
                                                        onSocketTimeout = {
                                                            Toast.makeText(
                                                                context,
                                                                R.string.remote_services_unavailable,
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    )
                                                }
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    } else {
                        null
                    }
                )
            }
            AnimatedMeetPointCard(
                visible = screenState is MeetPointScreenState.UpdateMeetPoint,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                clickedMeetPoint?.let {
                    val (meetPointName, onMeetPointNameChange) = rememberSaveable {
                        mutableStateOf(clickedMeetPoint.meetName)
                    }
                    val (meetPointDescription, onMeetPointDescription) = rememberSaveable {
                        mutableStateOf(clickedMeetPoint.meetDescription)
                    }
                    MeetPointCreationUpdateCard(
                        cardTitle = stringResource(R.string.update_meet_point),
                        meetPointName = meetPointName,
                        onMeetPointNameChange = onMeetPointNameChange,
                        meetPointDescription = meetPointDescription,
                        onMeetPointDescriptionChange = onMeetPointDescription,
                        onCloseButtonCLick = {
                            viewModel.showMeetPointDetails(clickedMeetPoint.id)
                        },
                        doneButton = if (viewModel.meetPointUpdatingInProgress) {
                            { CardProgressIndicator() }
                        } else {
                            null
                        },
                        onDoneButtonClick = {
                            val dataIsValid = meetPointDataIsValid(
                                meetPointName = meetPointName,
                                meetPointDescription = meetPointDescription,
                                onInvalidData = { resourceId ->
                                    Toast.makeText(
                                        context,
                                        resourceId,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                            if (!dataIsValid) {
                                return@MeetPointCreationUpdateCard
                            }
                            val updateMeetPoint = UpdateMeetPoint(
                                id = clickedMeetPoint.id,
                                meetName = meetPointName,
                                meetDescription = meetPointDescription
                            )
                            viewModel.updateMeetPoint(
                                updateMeetPoint,
                                onSuccess = {
                                    viewModel.showMeetPointDetails(clickedMeetPoint.id)
                                },
                                onFail = { _, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                },
                                onError = { error ->
                                    handleError(
                                        error,
                                        onConnectionFault = {
                                            Toast.makeText(
                                                context,
                                                R.string.no_internet_connection,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onSocketTimeout = {
                                            Toast.makeText(
                                                context,
                                                R.string.remote_services_unavailable,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            )
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedMeetPointCard(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
        content = content
    )
}

private fun meetPointDataIsValid(
    meetPointName: String,
    meetPointDescription: String,
    onInvalidData: (resourceId: Int) -> Unit
): Boolean =
    when {
        meetPointName.isEmpty() -> {
            onInvalidData(R.string.meet_point_name_cannot_be_empty)
            false
        }
        meetPointName.length > MAX_MEET_POINT_NAME_LENGTH -> {
            onInvalidData(R.string.max_length_of_meet_point_name_exceeded)
            false
        }
        meetPointDescription.length > MAX_MEET_POINT_DESCRIPTION_LENGTH -> {
            onInvalidData(R.string.meet_point_description_is_too_long)
            false
        }
        else -> {
            true
        }
    }

private val permissions = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
)
const val ANIMATION_DURATION_MS = 1000L
