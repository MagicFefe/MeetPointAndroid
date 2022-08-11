package com.swaptech.meet.domain.feedback.repository

import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse

interface FeedbackRepository {
    suspend fun sendFeedback(feedback: Feedback): NetworkResponse<Unit>
}
