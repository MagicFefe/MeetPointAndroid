package com.swaptech.meet.data.user

import android.content.SharedPreferences
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.domain.user.repository.UserRepository
import com.swaptech.meet.presentation.AUTH_TOKEN_KEY
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import com.swaptech.meet.presentation.utils.mappers.toUserDB
import com.swaptech.meet.presentation.utils.mappers.toUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val encryptedSharedPreferences: SharedPreferences
) : UserRepository {

    override suspend fun addLocalUser(user: UserResponse) {
        withContext(Dispatchers.IO) {
            userDao.addUser(user.toUserDB())
        }
    }

    override suspend fun deleteLocalUserById(userId: String) {
        userDao.deleteUserById(userId)
    }

    override fun getLocalUser(): Flow<UserResponse?> =
        userDao.getUser().map { userDb -> userDb?.toUserResponse() }

    override suspend fun getUserById(userId: String): NetworkResponse<UserResponse> =
        userApi.getUserById(userId)

    override suspend fun updateUser(user: UserUpdate): NetworkResponse<UserResponseWithToken> =
        userApi.updateUser(user)

    override suspend fun deleteUser(userId: String): NetworkResponse<Unit> =
        userApi.deleteUserById(userId)


    override suspend fun saveUserToken(token: String) {
        encryptedSharedPreferences.edit()
            .putString(AUTH_TOKEN_KEY, token)
            .apply()
    }

    override suspend fun deleteUserToken() {
        encryptedSharedPreferences.edit()
            .remove(AUTH_TOKEN_KEY)
            .apply()
    }
}
