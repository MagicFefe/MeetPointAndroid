package com.swaptech.meet.presentation.screen.home.user_screen.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.user.interactor.UserInteractor
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.presentation.utils.network_error_handling.onError
import com.swaptech.meet.presentation.utils.network_error_handling.onFail
import com.swaptech.meet.presentation.utils.network_error_handling.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserUpdateScreenViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
): ViewModel() {

    fun updateUser(
        user: UserUpdate,
        onSuccess: (UserResponseWithToken) -> Unit,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUser = userInteractor.updateUser(user)
            viewModelScope.launch(Dispatchers.Main) {
                newUser.onSuccess(onSuccess)
                    .onFail(onFail)
                    .onError(onError)
            }
        }
    }
}
