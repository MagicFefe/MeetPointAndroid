package com.swaptech.meet.data.feedback

import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedbackApi {

    @POST("feedback")
    suspend fun sendFeedback(@Body feedback: Feedback): NetworkResponse<Unit>
}
