package com.example.uap.model

import com.google.gson.annotations.SerializedName

// Model untuk respons yang berisi satu objek tanaman
data class SinglePlantResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Plant // Perbedaannya di sini, bukan List<Plant>
)