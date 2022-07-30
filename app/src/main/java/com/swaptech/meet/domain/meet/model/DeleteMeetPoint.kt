package com.swaptech.meet.domain.meet.model

import com.google.gson.annotations.SerializedName

data class DeleteMeetPoint(
    val id: String,
    @SerializedName("author_id")
    val authorId: String
)
