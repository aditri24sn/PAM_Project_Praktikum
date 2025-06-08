package com.example.uap.api

import com.example.uap.model.PlantResponse
import com.example.uap.model.SinglePlantResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("plant/all")
    suspend fun getAllPlants(): Response<PlantResponse>

    @POST("plant/new")
    suspend fun createPlant(@Body requestBody: RequestBody): Response<ResponseBody>

    // DIUBAH: Menggunakan {name} sebagai path dan parameternya String
    @GET("plant/{name}")
    suspend fun getPlantByName(@Path("name") plantName: String): Response<SinglePlantResponse>

    // DIUBAH: Menggunakan {name} sebagai path dan parameternya String
    @PUT("plant/{name}")
    suspend fun updatePlant(
        @Path("name") plantName: String,
        @Body requestBody: RequestBody
    ): Response<ResponseBody>

    // DIUBAH: Menggunakan {name} sebagai path dan parameternya String
    @DELETE("plant/{name}")
    suspend fun deletePlant(@Path("name") plantName: String): Response<ResponseBody>
}