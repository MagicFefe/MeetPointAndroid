package com.swaptech.meet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.user.interactor.UserInteractor
import com.swaptech.meet.domain.user.model.UserResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class RemoteUserViewModel @Inject constructor(
    private val userInteractor: UserInteractor
) : ViewModel() {

    suspend fun getUserById(userId: String): UserResponse =
        withContext(viewModelScope.coroutineContext) {
            userInteractor.getUserById(
                userId
            )
        }
}
