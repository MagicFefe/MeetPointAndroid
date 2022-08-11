package com.swaptech.meet.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.domain.auth.interactor.AuthInteractor
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import com.swaptech.meet.presentation.utils.network_error_handling.onError
import com.swaptech.meet.presentation.utils.network_error_handling.onFail
import com.swaptech.meet.presentation.utils.network_error_handling.onSuccess
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthUserViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val localUserViewModel: LocalUserViewModel
) : ViewModel() {

    private fun registerUser(
        block: suspend CoroutineScope.() -> NetworkResponse<UserResponseWithToken>,
        onSuccess: () -> Unit,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = block()
            viewModelScope.launch(Dispatchers.Main) {
                response
                    .onSuccess { user ->
                        localUserViewModel.saveLocalUser(user)
                        onSuccess()
                    }
                    .onFail(onFail)
                    .onError(onError)
            }
        }
    }

    fun signInUser(
        user: SignIn,
        onSuccess: () -> Unit,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        registerUser(
            block = {
                authInteractor.signInUser(user)
            },
            onSuccess = onSuccess,
            onFail = onFail,
            onError = onError
        )
    }

    fun signUpUser(
        signUp: SignUp,
        onSuccess: () -> Unit,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        registerUser(
            block = {
                authInteractor.signUpUser(signUp)
            },
            onSuccess = onSuccess,
            onFail = onFail,
            onError = onError
        )
    }
}
