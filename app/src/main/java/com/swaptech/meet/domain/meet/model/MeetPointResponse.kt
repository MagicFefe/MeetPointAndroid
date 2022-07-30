package com.swaptech.meet.domain.meet.model

import com.google.gson.annotations.SerializedName

data class MeetPointResponse(
    val id: String,
    @SerializedName("author_id")
    val authorId: String,
    @SerializedName("meet_name")
    val meetName: String,
    @SerializedName("meet_description")
    val meetDescription: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("created_at")
    val createdAt: String
)
