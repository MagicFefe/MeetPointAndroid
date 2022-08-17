package com.swaptech.meet.domain.map


interface MapPositionRepository {
    suspend fun save(mapPosition: MapPosition)
    suspend fun getMapCenter(): MapPosition?
    suspend fun deleteAll()
}
