package com.swaptech.meet.domain.feedback.repository

import com.swaptech.meet.domain.feedback.model.Feedback

interface FeedbackRepository {
    suspend fun sendFeedback(feedback: Feedback)
}
