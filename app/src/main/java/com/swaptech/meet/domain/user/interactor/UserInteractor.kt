package com.swaptech.meet.domain.user.interactor

import com.swaptech.meet.domain.user.model.UserMinimal
import com.swaptech.meet.domain.user.model.UserRegister
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun addLocalUser(user: UserResponse) {
        userRepository.addLocalUser(user)
    }

    suspend fun deleteLocalUserById(userId: String) {
        userRepository.deleteLocalUserById(userId)
    }

    fun getLocalUser(): Flow<UserResponse?> =
        userRepository.getLocalUser()

    suspend fun getUserById(userId: String): UserResponse =
        userRepository.getUserById(userId)

    suspend fun signInUser(user: UserMinimal): UserResponseWithToken =
        userRepository.signInUser(user)

    suspend fun signUpUser(user: UserRegister): UserResponseWithToken =
        userRepository.signUpUser(user)

    suspend fun updateUser(user: UserUpdate): UserResponseWithToken =
        userRepository.updateUser(user)

    suspend fun deleteUser(userId: String) {
        userRepository.deleteUser(userId)
    }

    suspend fun saveUserToken(token: String) {
        userRepository.saveUserToken(token)
    }

    suspend fun deleteUserToken() {
        userRepository.deleteUserToken()
    }
}
