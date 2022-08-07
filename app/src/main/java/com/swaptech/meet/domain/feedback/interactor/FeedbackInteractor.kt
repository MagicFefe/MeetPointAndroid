package com.swaptech.meet.domain.feedback.interactor

import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.domain.feedback.repository.FeedbackRepository
import javax.inject.Inject

class FeedbackInteractor @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {

    suspend fun sendFeedback(feedback: Feedback) {
        feedbackRepository.sendFeedback(feedback)
    }
}
