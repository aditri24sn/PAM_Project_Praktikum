package com.example.uap.ui.uii.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uap.model.Plant
import com.example.uap.api.ApiClient
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>> = _plantList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _deleteStatus = MutableLiveData<Result<String>>()
    val deleteStatus: LiveData<Result<String>> = _deleteStatus

    init {
        fetchPlants()
    }

    fun fetchPlants() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getAllPlants()

                if (response.isSuccessful) {
                    val plantData = response.body()?.data
                    // Log untuk sukses
                    Log.d("ViewModelData", "Response successful. Data count: ${plantData?.size ?: 0}")

                    if (plantData.isNullOrEmpty()) {
                        Log.w("ViewModelData", "Data dari API kosong atau null.")
                    }

                    _plantList.postValue(response.body()?.data)

                } else {
                    val errorBody = response.errorBody()?.string()
                    // Log untuk Gagal (misal: error 404 atau 500)
                    Log.e("ViewModelData", "Response failed. Code: ${response.code()}, Message: $errorBody")
                    _errorMessage.postValue("Gagal memuat data: Error ${response.code()}")
                }
            } catch (e: Exception) {
                // Log untuk Exception (misal: tidak ada koneksi internet)
                Log.e("ViewModelData", "Exception occurred: ${e.message}", e)
                _errorMessage.postValue("Terjadi kesalahan koneksi.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePlant(plantName: String) { // DIUBAH: parameter menjadi String
        viewModelScope.launch {
            try {
                // Panggil endpoint delete dari API
                val response = ApiClient.instance.deletePlant(plantName)

                if (response.isSuccessful) {
                    // Kirim status sukses
                    _deleteStatus.postValue(Result.success("Data berhasil dihapus"))
                    // Setelah berhasil dihapus, panggil lagi fetchPlants untuk me-refresh daftar
                    fetchPlants()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _deleteStatus.postValue(Result.failure(Exception("Gagal menghapus data: $errorBody")))
                }
            } catch (e: Exception) {
                // Tangani jika ada error koneksi
                _deleteStatus.postValue(Result.failure(e))
            }
        }
    }
}