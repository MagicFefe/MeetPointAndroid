package com.swaptech.meet.data.map

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_center")
data class MapPositionDB(
    @PrimaryKey val id: Int = 1,
    val latitude: Double,
    val longitude: Double,
    val mapCenterOffsetX: Int,
    val mapCenterOffsetY: Int,
    val zoomLevel: Double
)
