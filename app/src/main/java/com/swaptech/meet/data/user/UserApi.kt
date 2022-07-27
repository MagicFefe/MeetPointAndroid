package com.swaptech.meet.data.user

import com.swaptech.meet.domain.user.model.UserRegister
import com.swaptech.meet.domain.user.model.UserMinimal
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("user/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: String): UserResponse

    @POST("user/sign_in")
    suspend fun signInUser(@Body user: UserMinimal): UserResponseWithToken

    @POST("user/sign_up")
    suspend fun signUpUser(@Body user: UserRegister): UserResponseWithToken

    @PUT("user")
    suspend fun updateUser(@Body user: UserUpdate): UserResponseWithToken

    @DELETE("user/{user_id}")
    suspend fun deleteUserById(@Path("user_id") userId: String)
}
