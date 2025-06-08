package com.example.uap.model

import com.google.gson.annotations.SerializedName

data class SinglePlantResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Plant
)