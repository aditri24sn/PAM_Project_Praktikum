package com.example.uap.model
import com.google.gson.annotations.SerializedName

data class Plant(
    @SerializedName("id")
    val id: Int,

    @SerializedName("plant_name")
    val plantName: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("image_url")
    val imageUrl: String? = null
)