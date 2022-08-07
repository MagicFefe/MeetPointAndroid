package com.swaptech.meet.presentation.screen.home.more.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.feedback.interactor.FeedbackInteractor
import com.swaptech.meet.domain.feedback.model.Feedback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedbackScreenViewModel @Inject constructor(
    private val feedbackInteractor: FeedbackInteractor
): ViewModel() {

    fun sendFeedback(feedback: Feedback) {
        viewModelScope.launch(Dispatchers.IO) {
            feedbackInteractor.sendFeedback(feedback)
        }
    }
}
