package com.swaptech.meet.domain.auth.repository

import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse

interface AuthRepository {
    suspend fun signInUser(user: SignIn): NetworkResponse<UserResponseWithToken>
    suspend fun signUpUser(user: SignUp): NetworkResponse<UserResponseWithToken>
}
