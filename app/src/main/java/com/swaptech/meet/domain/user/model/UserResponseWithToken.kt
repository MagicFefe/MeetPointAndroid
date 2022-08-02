package com.swaptech.meet.domain.user.model

import com.google.gson.annotations.SerializedName

data class UserResponseWithToken(
    val id: String,
    val name: String,
    val surname: String,
    val about: String,
    val dob: String,
    val email: String,
    val country: String,
    val city: String,
    @SerializedName("jwt")
    val token: String,
    val image: String
)
