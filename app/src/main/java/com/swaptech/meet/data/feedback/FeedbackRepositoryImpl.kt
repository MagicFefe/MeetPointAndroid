package com.swaptech.meet.data.feedback

import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.domain.feedback.repository.FeedbackRepository
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val feedbackApi: FeedbackApi
): FeedbackRepository {

    override suspend fun sendFeedback(feedback: Feedback): NetworkResponse<Unit> =
        feedbackApi.sendFeedback(feedback)
}
