package com.swaptech.meet.domain.user.model


data class UserResponse(
    val id: String,
    val name: String,
    val surname: String,
    val about: String,
    val dob: String,
    val gender: String,
    val email: String,
    val country: String,
    val city: String,
    val image: String
)
