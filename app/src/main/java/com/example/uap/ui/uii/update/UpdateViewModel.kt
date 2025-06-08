package com.example.uap.ui.uii.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uap.api.ApiClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class UpdateViewModel : ViewModel() {

    private val _updateResult = MutableLiveData<Result<String>>()
    val updateResult: LiveData<Result<String>> = _updateResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun updatePlant(originalName: String, newName: String, newDescription: String, newPrice: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Body JSON sekarang menggunakan data baru
                val jsonObject = JSONObject().apply {
                    put("plant_name", newName)
                    put("description", newDescription)
                    put("price", newPrice)
                }
                val requestBody = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                // Panggil endpoint API dengan nama asli sebagai identifier di URL
                val response = ApiClient.instance.updatePlant(originalName, requestBody)

                if (response.isSuccessful) {
                    _updateResult.postValue(Result.success("Data berhasil diperbarui"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    _updateResult.postValue(Result.failure(Exception("Gagal memperbarui data: $errorBody")))
                }
            } catch (e: Exception) {
                _updateResult.postValue(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}