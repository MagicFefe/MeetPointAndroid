package com.swaptech.meet.domain.user.model


data class UserRegister(
    val name: String,
    val surname: String,
    val email: String,
    val country: String,
    val city: String,
    val password: String,
    val image: String?
)
