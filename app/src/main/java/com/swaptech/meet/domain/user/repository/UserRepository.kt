package com.swaptech.meet.domain.user.repository

import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addLocalUser(user: UserResponse)
    suspend fun deleteLocalUserById(userId: String)
    fun getLocalUser(): Flow<UserResponse?>
    suspend fun getUserById(userId: String): NetworkResponse<UserResponse>
    suspend fun updateUser(user: UserUpdate): NetworkResponse<UserResponseWithToken>
    suspend fun deleteUser(userId: String): NetworkResponse<Unit>
    suspend fun saveUserToken(token: String)
    suspend fun deleteUserToken()
}
