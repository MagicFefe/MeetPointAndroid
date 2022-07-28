package com.swaptech.meet.presentation.screen.home.user_screen.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.user.interactor.UserInteractor
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class UserUpdateScreenViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
): ViewModel() {

    fun updateUser(
        user: UserUpdate,
        onSuccess: (UserResponseWithToken) -> Unit,
        onHttpError: (HttpException) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newUser = userInteractor.updateUser(user)
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess(newUser)
                }
            } catch (httpException: HttpException) {
                viewModelScope.launch(Dispatchers.Main) {
                    onHttpError(httpException)
                }
            }
        }
    }
}
