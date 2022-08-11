package com.swaptech.meet.domain.feedback.interactor

import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.domain.feedback.repository.FeedbackRepository
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import javax.inject.Inject

class FeedbackInteractor @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {

    suspend fun sendFeedback(feedback: Feedback): NetworkResponse<Unit> =
        feedbackRepository.sendFeedback(feedback)
}
