package com.swaptech.meet.presentation.utils

import android.graphics.Color
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swaptech.meet.R
import com.swaptech.meet.domain.meet.MeetPointResponse
import com.swaptech.meet.domain.meet.MeetPointResponseDetails
import com.swaptech.meet.presentation.MAPNIK_512
import com.swaptech.meet.presentation.WORLD_LEVEL_ZOOM
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
    centerLatitude: Double = 0.0,
    centerLongitude: Double = 0.0,
    zoomLevel: Double = WORLD_LEVEL_ZOOM,
    newMeetPoints: List<MeetPointResponse>,
    oldMeetPoints: List<MeetPointResponse>,
    onFirstLocationFix: (MyLocationNewOverlay, MapView) -> Unit,
    onMapSingleTap: (GeoPoint?, MapView) -> Boolean,
    onMarkerClickListener: (Marker, MapView) -> Boolean,
    onDisposeMap: (MapView) -> Unit
) {
    val owner = LocalLifecycleOwner.current
    val localContext = LocalContext.current
    val map = MapView(localContext)
    LaunchedEffect(key1 = Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Event.ON_RESUME -> {
                    map.onResume()
                }
                Event.ON_PAUSE -> {
                    map.onPause()
                }
                else -> {}
            }
        }
        owner.lifecycle.addObserver(observer)
    }
    AndroidView(
        factory = { context ->
            val mapEventsReceiver = object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean =
                    onMapSingleTap(p, map)

                override fun longPressHelper(p: GeoPoint?): Boolean = false
            }
            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            val compassOverlay =
                CompassOverlay(context, InternalCompassOrientationProvider(context), map)
            val rotationGestureOverlay = RotationGestureOverlay(map)
            val scaleBarOverlay = ScaleBarOverlay(map)
            val locationOverlay =
                MyLocationNewOverlay(GpsMyLocationProvider(localContext), map).apply {
                    onFirstLocationFix(this, map)
                }

            return@AndroidView map.apply {
                setTileSource(MAPNIK_512)

                controller.setCenter(
                    GeoPoint(
                        centerLatitude,
                        centerLongitude
                    )
                )
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

                controller.setZoom(zoomLevel)

                overlays.add(locationOverlay)
                locationOverlay.enableMyLocation()
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
                        //TODO: Add Description
                        setOnMarkerClickListener(onMarkerClickListener)
                        title = meetPointResponse.id
                        icon =
                            ContextCompat.getDrawable(
                                localContext,
                                R.drawable.ic_baseline_circle_24
                            )
                                ?.apply {
                                    colorFilter =
                                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                                            Color.BLUE, BlendModeCompat.SRC_ATOP
                                        )
                                }
                        position = GeoPoint(
                            meetPointResponse.latitude,
                            meetPointResponse.longitude
                        )
                    }

                it.overlays.add(meet)
            }
        }
    )
    DisposableEffect(Unit) {
        onDispose {
            onDisposeMap(map)
        }
    }
}

@Composable
fun MeetPointCreationCard(
    meetPointName: String,
    onMeetPointNameChange: (String) -> Unit,
    meetPointDescription: String,
    onMeetPointDescriptionChange: (String) -> Unit,
    onCloseButtonCLick: () -> Unit,
    onDoneButtonClick: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 3.dp, top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseButtonCLick
                ) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
                Text(
                    text = stringResource(id = R.string.create_meet_point),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.padding(end = 3.dp),
                    onClick = onDoneButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = null
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp),
                value = meetPointName,
                onValueChange = onMeetPointNameChange,
                label = {
                    Text(text = stringResource(id = R.string.meet_point_name))
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp),
                value = meetPointDescription,
                onValueChange = onMeetPointDescriptionChange,
                label = {
                    Text(text = stringResource(id = R.string.meet_point_description))
                }
            )
        }
    }
}

@Preview
@Composable
fun MeetPointCreationMin_Preview() {
    val (name, onNameChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (description, onDescriptionChange) = rememberSaveable {
        mutableStateOf("")
    }
    MeetPointCreationCard(
        name,
        onNameChange,
        description,
        onDescriptionChange,
        {},
        {}
    )
}

@Composable
fun MeetPointDetails(
    modifier: Modifier = Modifier,
    meetPoint: MeetPointResponseDetails,
    onCloseButtonCLick: () -> Unit,
    actionButton: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 3.dp, top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseButtonCLick
                ) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
                Text(
                    text = stringResource(id = R.string.meet_point_details),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                actionButton?.let {
                    actionButton()
                }
            }
            Column {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val decodedProfileImage = Base64.decode(meetPoint.authorImage, Base64.DEFAULT)
                    UserHeader(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(20.dp),
                        profileName = meetPoint.authorName,
                        profileSurname = meetPoint.authorSurname,
                        profileImage = decodedProfileImage
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 17.dp)
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.meet_point_name_with_params,
                            meetPoint.meetName
                        )
                    )
                    Text(
                        text = stringResource(
                            id = R.string.meet_point_description_with_params,
                            meetPoint.meetDescription
                        )
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = stringResource(id = R.string.meet_point_created_at, meetPoint.createdAt)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MeetPointDetails_Preview() {
    MeetPointDetails(
        meetPoint = MeetPointResponseDetails(
            id = "",
            authorId = "",
            meetName = "Talk with new friends",
            meetDescription = "Some description",
            latitude = 2.0,
            longitude = 2.0,
            createdAt = "23.07.2022",
            authorName = "Ivan",
            authorSurname = "Ivanov",
            authorImage = ""
        ),
        onCloseButtonCLick = {},
        actionButton = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
            }
        }
    )
}

@Composable
fun UserImage(
    modifier: Modifier,
    userImage: ByteArray?
) {
    if (userImage != null) {
        AsyncImage(
            modifier = modifier.clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(userImage)
                .placeholder(R.drawable.user_image_placeholder)
                .build(),
            contentDescription = null
        )
    } else {
        Image(
            modifier = modifier.clip(CircleShape),
            painter = painterResource(R.drawable.user_image_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun UserHeader(
    modifier: Modifier,
    profileName: String,
    profileSurname: String,
    profileImage: ByteArray?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserImage(
            modifier = modifier,
            userImage = profileImage
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = profileName,
            fontSize = 16.sp
        )
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = profileSurname,
            fontSize = 16.sp
        )
    }
}
