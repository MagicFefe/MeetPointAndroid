package com.swaptech.meet.data.map

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MapPositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(mapPositionDB: MapPositionDB)

    @Query("SELECT * FROM map_center LIMIT 1")
    suspend fun getMapCenter(): MapPositionDB?

    @Query("DELETE FROM map_center")
    suspend fun deleteAll()
}
