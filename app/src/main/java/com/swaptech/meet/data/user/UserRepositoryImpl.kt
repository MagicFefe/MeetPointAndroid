package com.swaptech.meet.data.user

import android.content.SharedPreferences
import com.swaptech.meet.domain.user.model.UserMinimal
import com.swaptech.meet.domain.user.model.UserRegister
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.domain.user.repository.UserRepository
import com.swaptech.meet.presentation.AUTH_TOKEN_KEY
import com.swaptech.meet.presentation.utils.toUserDB
import com.swaptech.meet.presentation.utils.toUserResponse
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

    override suspend fun getUserById(userId: String): UserResponse =
        userApi.getUserById(userId)

    override suspend fun signInUser(user: UserMinimal): UserResponseWithToken =
        userApi.signInUser(user)

    override suspend fun signUpUser(user: UserRegister): UserResponseWithToken =
        userApi.signUpUser(user)

    override suspend fun updateUser(user: UserUpdate): UserResponseWithToken =
        userApi.updateUser(user)

    override suspend fun deleteUser(userId: String) {
        userApi.deleteUserById(userId)
    }

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
