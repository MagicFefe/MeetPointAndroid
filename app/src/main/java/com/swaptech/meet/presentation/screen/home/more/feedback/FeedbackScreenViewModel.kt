package com.swaptech.meet.presentation.screen.home.more.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.feedback.interactor.FeedbackInteractor
import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.presentation.utils.network_error_handling.onError
import com.swaptech.meet.presentation.utils.network_error_handling.onFail
import com.swaptech.meet.presentation.utils.network_error_handling.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedbackScreenViewModel @Inject constructor(
    private val feedbackInteractor: FeedbackInteractor
): ViewModel() {

    fun sendFeedback(
        feedback: Feedback,
        onSuccess: (Unit) -> Unit,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = feedbackInteractor.sendFeedback(feedback)
            viewModelScope.launch(Dispatchers.Main){
                response.onSuccess(onSuccess)
                    .onFail(onFail)
                    .onError(onError)
            }
        }
    }
}
