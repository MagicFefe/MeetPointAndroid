package com.swaptech.meet.domain.auth.repository

import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.user.model.UserResponseWithToken

interface AuthRepository {
    suspend fun signInUser(user: SignIn): UserResponseWithToken
    suspend fun signUpUser(user: SignUp): UserResponseWithToken
}
