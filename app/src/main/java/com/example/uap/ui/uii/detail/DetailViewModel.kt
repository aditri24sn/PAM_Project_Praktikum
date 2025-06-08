package com.example.uap.ui.uii.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uap.api.ApiClient
import com.example.uap.model.Plant
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _plantDetail = MutableLiveData<Plant>()
    val plantDetail: LiveData<Plant> = _plantDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchPlantDetail(plantName: String) { // DIUBAH: parameter menjadi String
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getPlantByName(plantName)
                if (response.isSuccessful) {
                    _plantDetail.postValue(response.body()?.data)
                } else {
                    _errorMessage.postValue("Gagal memuat detail: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Terjadi kesalahan: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}