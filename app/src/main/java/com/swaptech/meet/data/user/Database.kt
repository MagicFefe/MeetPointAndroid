package com.swaptech.meet.data.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.swaptech.meet.data.map.MapPositionDB
import com.swaptech.meet.data.map.MapPositionDao

@Database(
    entities = [
        UserDB::class, MapPositionDB::class
    ],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getMapPositionDao(): MapPositionDao
}
