package com.swaptech.meet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.user.interactor.UserInteractor
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import com.swaptech.meet.presentation.utils.network_error_handling.onError
import com.swaptech.meet.presentation.utils.network_error_handling.onFail
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class RemoteUserViewModel @Inject constructor(
    private val userInteractor: UserInteractor
) : ViewModel() {

    suspend fun getUserById(
        userId: String,
        onFail: suspend (Int, String) -> Unit,
        onError: suspend (Throwable) -> Unit
    ): NetworkResponse<UserResponse> =
        withContext(viewModelScope.coroutineContext) {
            val response = userInteractor.getUserById(
                userId
            )
            response.onFail(onFail)
                .onError(onError)
        }
}
