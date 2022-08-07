package com.swaptech.meet.data.auth

import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.auth.repository.AuthRepository
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): AuthRepository {

    override suspend fun signInUser(user: SignIn): UserResponseWithToken =
        authApi.signInUser(user)

    override suspend fun signUpUser(user: SignUp): UserResponseWithToken =
        authApi.signUpUser(user)
}
