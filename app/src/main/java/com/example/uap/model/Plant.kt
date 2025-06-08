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
    val price: String, // Sesuai spek API, harga adalah VARCHAR (String)

    // Untuk sementara, kita asumsikan ada URL gambar di API
    // Jika tidak ada, Anda bisa menghapus atau mengabaikan properti ini.
    @SerializedName("image_url")
    val imageUrl: String? = null
)