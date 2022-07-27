package com.swaptech.meet.domain.user.model

import com.google.gson.annotations.SerializedName

data class UserUpdate(
    @SerializedName("new_email")
    val newEmail: String, //Gson excludes
    @SerializedName("old_email")
    val oldEmail: String, //Gson excludes
    @SerializedName("old_password")
    val oldPassword: String, //Gson excludes
    @SerializedName("new_password")
    val newPassword: String, //Gson excludes
    val id: String,
    val name: String, //Gson excludes
    val surname: String, //Gson excludes
    val country: String,
    val city: String,
    val image: String
)
