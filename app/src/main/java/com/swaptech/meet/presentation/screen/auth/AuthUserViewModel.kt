package com.swaptech.meet.presentation.screen.auth

import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.user.interactor.UserInteractor
import com.swaptech.meet.domain.user.model.UserRegister
import com.swaptech.meet.domain.user.model.UserMinimal
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.utils.toUserResponse
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class AuthUserViewModel @Inject constructor(
    private val userInteractor: UserInteractor
) : LocalUserViewModel(userInteractor) {

    private fun registerUser(
        block: suspend CoroutineScope.() -> UserResponseWithToken,
        onSuccess: () -> Unit,
        onHttpError: (HttpException) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = block()
                saveLocalUser(user)
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (httpException: HttpException) {
                viewModelScope.launch(Dispatchers.Main) {
                    onHttpError(httpException)
                }
            }
        }
    }

    fun signInUser(
        user: UserMinimal,
        onSuccess: () -> Unit,
        onHttpError: (HttpException) -> Unit
    ) {
        registerUser(
            block = {
                userInteractor.signInUser(user)
            },
            onSuccess = onSuccess,
            onHttpError = onHttpError
        )
    }

    fun signUpUser(
        userRegister: UserRegister,
        onSuccess: () -> Unit,
        onHttpError: (HttpException) -> Unit
    ) {
        registerUser(
            block = {
                userInteractor.signUpUser(userRegister)
            },
            onSuccess = onSuccess,
            onHttpError = onHttpError
        )
    }
}
