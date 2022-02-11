package com.example.ibm.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FilmData(
    val guid: String = "",
    val email: String = "",
    val userName: String = "",
    val description: String = "",
    val title: String = "",
    @SerializedName("avatarURL")
    val avatarUrl: String = "",
    val created: String = "",
    val durationInSec: Int = 2
) : Serializable
