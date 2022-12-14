package com.swaptech.meet.domain.auth.interactor

import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.auth.repository.AuthRepository
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun signInUser(user: SignIn): NetworkResponse<UserResponseWithToken> =
        authRepository.signInUser(user)

    suspend fun signUpUser(user: SignUp): NetworkResponse<UserResponseWithToken> =
        authRepository.signUpUser(user)
}
