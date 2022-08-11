package com.swaptech.meet.data.auth

import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/sign_in")
    suspend fun signInUser(@Body user: SignIn): NetworkResponse<UserResponseWithToken>

    @POST("auth/sign_up")
    suspend fun signUpUser(@Body user: SignUp): NetworkResponse<UserResponseWithToken>
}
