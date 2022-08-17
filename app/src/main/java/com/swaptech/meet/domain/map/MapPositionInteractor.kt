package com.swaptech.meet.domain.map

import javax.inject.Inject

class MapPositionInteractor @Inject constructor(
    private val mapPositionRepository: MapPositionRepository
) {

    suspend fun save(mapPosition: MapPosition) =
        mapPositionRepository.save(mapPosition)

    suspend fun getMapCenter(): MapPosition? =
        mapPositionRepository.getMapCenter()

    suspend fun deleteAll() =
        mapPositionRepository.deleteAll()
}
