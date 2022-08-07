package com.swaptech.meet.domain.feedback.model

import com.google.gson.annotations.SerializedName

data class Feedback(
    @SerializedName("user_id")
    val userId: String,
    val message: String
)
