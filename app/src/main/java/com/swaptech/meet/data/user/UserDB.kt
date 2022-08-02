package com.swaptech.meet.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDB(
    @PrimaryKey
    val id: String,
    val name: String,
    val surname: String,
    val dob: String,
    val about: String,
    val email: String,
    val country: String,
    val city: String
)
