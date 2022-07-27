package com.swaptech.meet.presentation.screen.home.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.user.interactor.UserInteractor
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class UserScreenViewModel @Inject constructor(
    private val userInteractor: UserInteractor
) : LocalUserViewModel(userInteractor) {

    var screenState: UserScreenState by mutableStateOf(UserScreenState.Idle)
        private set
    var userById: UserResponse? by mutableStateOf(null)
        private set

    fun getUserById(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userById = userInteractor.getUserById(userId)
        }
    }

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

    fun startUpdateUser() {
        screenState = UserScreenState.Update
    }

    fun finishUpdateUser() {
        screenState = UserScreenState.Idle
    }
}
