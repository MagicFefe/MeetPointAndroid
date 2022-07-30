package com.swaptech.meet.domain.meet.model

import com.google.gson.annotations.SerializedName

data class UpdateMeetPoint(
    val id: String,
    @SerializedName("meet_name")
    val meetName: String,
    @SerializedName("meet_description")
    val meetDescription: String
)
