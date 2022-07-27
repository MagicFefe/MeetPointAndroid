package com.swaptech.meet.domain.meet

import com.google.gson.annotations.SerializedName

data class MeetPointResponseDetails(
    val id: String,
    @SerializedName("author_id")
    val authorId: String,
    @SerializedName("author_name")
    val authorName: String,
    @SerializedName("author_surname")
    val authorSurname: String,
    @SerializedName("author_image")
    val authorImage: String,
    @SerializedName("meet_name")
    val meetName: String,
    @SerializedName("meet_description")
    val meetDescription: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("created_at")
    val createdAt: String
)
