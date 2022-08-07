package com.swaptech.meet.data.feedback

import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.domain.feedback.repository.FeedbackRepository
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val feedbackApi: FeedbackApi
): FeedbackRepository {

    override suspend fun sendFeedback(feedback: Feedback) {
        feedbackApi.sendFeedback(feedback)
    }
}
