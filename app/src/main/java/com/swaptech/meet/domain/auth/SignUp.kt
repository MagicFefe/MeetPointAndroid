package com.swaptech.meet.domain.auth


data class SignUp(
    val name: String,
    val surname: String,
    val about: String,
    val dob: String,
    val gender: String,
    val email: String,
    val country: String,
    val city: String,
    val password: String,
    val image: String?
)
