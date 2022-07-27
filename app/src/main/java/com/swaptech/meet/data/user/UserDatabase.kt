package com.swaptech.meet.data.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserDB::class
    ],
    version = 1
)
abstract class UserDatabase: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}
