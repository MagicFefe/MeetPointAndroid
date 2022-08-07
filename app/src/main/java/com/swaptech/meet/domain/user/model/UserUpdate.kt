package com.swaptech.meet.domain.user.model

import com.google.gson.annotations.SerializedName

data class UserUpdate(
    val email: String,
    @SerializedName("old_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String,
    val id: String,
    val name: String,
    val surname: String,
    val about: String,
    val dob: String,
    val gender: String,
    val country: String,
    val city: String,
    val image: String
)
