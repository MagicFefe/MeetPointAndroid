package com.swaptech.meet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.user.interactor.UserInteractor
import com.swaptech.meet.domain.user.model.UserResponseWithToken
import com.swaptech.meet.presentation.utils.toUserResponse
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

open class LocalUserViewModel @Inject constructor(
    private val userInteractor: UserInteractor
) : ViewModel() {

    val localUser = userInteractor.getLocalUser()

    /**
     * it using for sequential execution of coroutines,
     * because in UserUpdateScreen when we starting deletion old user
     * and inserting new user, first starting saving user token,
     * and after that starting deleting user token
     */
    private val pipeLineChannel = Channel<Job>().apply {
        viewModelScope.launch(Dispatchers.IO) {
            consumeEach { job ->
                job.join()
            }
        }
    }

    fun saveLocalUser(user: UserResponseWithToken) {
        viewModelScope.launch {
            val job = viewModelScope.launch(context = Dispatchers.IO, start = CoroutineStart.LAZY) {
                userInteractor.addLocalUser(user.toUserResponse())
                userInteractor.saveUserToken(user.token)
            }
            pipeLineChannel.send(job)
        }
    }

    fun deleteLocalUserById(userId: String) {
        viewModelScope.launch {
            val job = viewModelScope.launch(context = Dispatchers.IO, start = CoroutineStart.LAZY) {
                userInteractor.deleteLocalUserById(userId)
                userInteractor.deleteUserToken()
            }
            pipeLineChannel.send(job)
        }
    }
}
