package com.example.uap.model

import com.google.gson.annotations.SerializedName

// Class ini mewakili seluruh objek respons dari API
data class PlantResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    // Properti 'data' ini berisi list/array dari objek Tanaman (Plant)
    @SerializedName("data")
    val data: List<Plant>
)