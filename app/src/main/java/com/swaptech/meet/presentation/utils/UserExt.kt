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
        city = city
    )

fun UserDB.toUserResponse(): UserResponse =
    UserResponse(
        id = id,
        name = name,
        surname = surname,
        email = email,
        country = country,
        city = city,
        image = ""
    )

fun UserResponseWithToken.toUserResponse(): UserResponse =
    UserResponse(
        id = id,
        name = name,
        surname = surname,
        email = email,
        country = country,
        city = city,
        image = image
    )
