package com.swaptech.meet.domain.map


data class MapPosition(
    val latitude: Double,
    val longitude: Double,
    val mapCenterOffsetX: Int,
    val mapCenterOffsetY: Int,
    val zoomLevel: Double
)
