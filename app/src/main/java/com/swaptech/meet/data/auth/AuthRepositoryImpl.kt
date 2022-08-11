package com.swaptech.meet.data.auth

import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.auth.repository.AuthRepository
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): AuthRepository {

    override suspend fun signInUser(user: SignIn): NetworkResponse<UserResponseWithToken> =
        authApi.signInUser(user)

    override suspend fun signUpUser(user: SignUp): NetworkResponse<UserResponseWithToken> =
        authApi.signUpUser(user)
}
