package com.swaptech.meet.presentation.utils.mappers

import com.swaptech.meet.data.map.MapPositionDB
import com.swaptech.meet.domain.map.MapPosition

fun MapPosition.toMapPositionDb() =
    MapPositionDB(
        latitude = latitude,
        longitude = longitude,
        mapCenterOffsetX = mapCenterOffsetX,
        mapCenterOffsetY = mapCenterOffsetY,
        zoomLevel = zoomLevel
    )

fun MapPositionDB.toMapPosition() =
    MapPosition(
        latitude = latitude,
        longitude = longitude,
        mapCenterOffsetX = mapCenterOffsetX,
        mapCenterOffsetY = mapCenterOffsetY,
        zoomLevel = zoomLevel
    )
