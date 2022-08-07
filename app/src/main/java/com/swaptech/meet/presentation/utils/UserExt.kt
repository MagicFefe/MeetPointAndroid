package com.swaptech.meet.presentation.utils

import com.swaptech.meet.data.user.UserDB
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken

fun UserResponse.toUserDB(): UserDB =
    UserDB(
        id = id,
        name = name,
        surname = surname,
        email = email,
        country = country,
        city = city,
        dob = dob,
        gender = gender,
        about = about
    )

fun UserDB.toUserResponse(): UserResponse =
    UserResponse(
        id = id,
        name = name,
        surname = surname,
        email = email,
        country = country,
        city = city,
        image = "",
        dob = dob,
        gender = gender,
        about = about,
    )

fun UserResponseWithToken.toUserResponse(): UserResponse =
    UserResponse(
        id = id,
        name = name,
        surname = surname,
        email = email,
        country = country,
        city = city,
        image = image,
        dob = dob,
        gender = gender,
        about = about
    )
