package com.swaptech.meet.data.map

import com.swaptech.meet.domain.map.MapPosition
import com.swaptech.meet.domain.map.MapPositionRepository
import com.swaptech.meet.presentation.utils.mappers.toMapPosition
import com.swaptech.meet.presentation.utils.mappers.toMapPositionDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MapPositionRepositoryImpl @Inject constructor(
    private val mapPositionDao: MapPositionDao
): MapPositionRepository {

    override suspend fun save(mapPosition: MapPosition) {
        mapPositionDao.save(mapPosition.toMapPositionDb())
    }

    override suspend fun getMapCenter(): MapPosition? =
        mapPositionDao.getMapCenter()?.toMapPosition()

    override suspend fun deleteAll() {
        mapPositionDao.deleteAll()
    }
}
