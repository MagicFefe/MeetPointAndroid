package com.swaptech.meet.presentation.utils.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable

data class MeetPointMapState(
    val latitude: Double,
    val longitude: Double,
    val mapCenterOffsetX: Int,
    val mapCenterOffsetY: Int,
    val zoomLevel: Double
)

val MeetPointMapStateSaver = run {
    val latitudeKey = "latitude"
    val longitudeKey = "longitude"
    val mapCenterOffsetXKey = "mapCenterOffsetX"
    val mapCenterOffsetYKey = "mapCenterOffsetY"
    val zoomLevelKey = "zoomLevel"
    mapSaver(
        save = { mapState ->
               mapOf(
                   latitudeKey to mapState.latitude,
                   longitudeKey to mapState.longitude,
                   mapCenterOffsetXKey to mapState.mapCenterOffsetX,
                   mapCenterOffsetYKey to mapState.mapCenterOffsetY,
                   zoomLevelKey to mapState.zoomLevel,
               )
        },
        restore = {
            MeetPointMapState(
                latitude = it[latitudeKey] as Double,
                longitude = it[longitudeKey] as Double,
                mapCenterOffsetX = it[mapCenterOffsetXKey] as Int,
                mapCenterOffsetY = it[mapCenterOffsetYKey] as Int,
                zoomLevel = it[zoomLevelKey] as Double
            )
        }
    )
}

@Composable
fun rememberMeetPointMapState(
    latitude: Double,
    longitude: Double,
    mapCenterOffsetX: Int,
    mapCenterOffsetY: Int,
    zoomLevel: Double
) = rememberSaveable(
    inputs = arrayOf(latitude, longitude, mapCenterOffsetX, mapCenterOffsetY, zoomLevel),
    stateSaver = MeetPointMapStateSaver
) {
    mutableStateOf(
        MeetPointMapState(
            latitude = latitude,
            longitude = longitude,
            mapCenterOffsetX = mapCenterOffsetX,
            mapCenterOffsetY = mapCenterOffsetY,
            zoomLevel = zoomLevel
        )
    )
}
