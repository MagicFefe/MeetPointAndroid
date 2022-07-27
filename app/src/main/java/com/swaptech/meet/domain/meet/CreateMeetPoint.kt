package com.swaptech.meet.domain.meet

import com.google.gson.annotations.SerializedName

data class CreateMeetPoint(
    @SerializedName("meet_name")
    val meetName: String,
    @SerializedName("meet_description")
    val meetDescription: String,
    @SerializedName("author_id")
    val authorId: String,
    @SerializedName("author_name")
    val authorName: String,
    @SerializedName("author_surname")
    val authorSurname: String,
    val latitude: Double,
    val longitude: Double
)
