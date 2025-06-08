package com.example.uap.ui.uii.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uap.api.ApiClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AddViewModel : ViewModel() {

    // LiveData untuk mengirimkan hasil (sukses/gagal) ke Activity
    private val _addResult = MutableLiveData<Result<String>>()
    val addResult: LiveData<Result<String>> = _addResult

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addPlant(name: String, description: String, price: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // 1. Membuat objek JSON dari input pengguna
                val jsonObject = JSONObject().apply {
                    put("plant_name", name)
                    put("description", description)
                    put("price", price)
                }

                // 2. Mengubah string JSON menjadi RequestBody yang bisa dikirim oleh Retrofit
                val requestBody = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                // 3. Memanggil API untuk membuat data baru
                val response = ApiClient.instance.createPlant(requestBody)

                if (response.isSuccessful) {
                    _addResult.postValue(Result.success("Data berhasil ditambahkan"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    _addResult.postValue(Result.failure(Exception("Gagal: $errorBody")))
                }

            } catch (e: Exception) {
                _addResult.postValue(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}