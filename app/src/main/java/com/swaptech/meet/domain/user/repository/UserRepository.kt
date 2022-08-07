package com.swaptech.meet.domain.user.repository

import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addLocalUser(user: UserResponse)
    suspend fun deleteLocalUserById(userId: String)
    fun getLocalUser(): Flow<UserResponse?>
    suspend fun getUserById(userId: String): UserResponse
    suspend fun updateUser(user: UserUpdate): UserResponseWithToken
    suspend fun deleteUser(userId: String)
    suspend fun saveUserToken(token: String)
    suspend fun deleteUserToken()
}
