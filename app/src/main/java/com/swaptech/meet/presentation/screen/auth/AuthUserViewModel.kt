package com.swaptech.meet.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.auth.interactor.AuthInteractor
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class AuthUserViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val localUserViewModel: LocalUserViewModel
) : ViewModel() {

    private fun registerUser(
        block: suspend CoroutineScope.() -> UserResponseWithToken,
        onSuccess: () -> Unit,
        onHttpError: (HttpException) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = block()
                localUserViewModel.saveLocalUser(user)
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
        user: SignIn,
        onSuccess: () -> Unit,
        onHttpError: (HttpException) -> Unit
    ) {
        registerUser(
            block = {
                authInteractor.signInUser(user)
            },
            onSuccess = onSuccess,
            onHttpError = onHttpError
        )
    }

    fun signUpUser(
        signUp: SignUp,
        onSuccess: () -> Unit,
        onHttpError: (HttpException) -> Unit
    ) {
        registerUser(
            block = {
                authInteractor.signUpUser(signUp)
            },
            onSuccess = onSuccess,
            onHttpError = onHttpError
        )
    }
}
