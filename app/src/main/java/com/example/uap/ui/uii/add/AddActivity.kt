package com.example.uap.ui.uii.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.uap.databinding.ActivityAddBinding // Pastikan nama binding ini benar

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private val addViewModel: AddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur listener untuk tombol Tambah
        binding.btnSubmit.setOnClickListener {
            val name = binding.etNama.text.toString().trim()
            val price = binding.etHarga.text.toString().trim()
            val description = binding.etDeskripsi.text.toString().trim()

            // Validasi input tidak boleh kosong
            if (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()) {
                addViewModel.addPlant(name, description, price)
            } else {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // Panggil fungsi untuk mengamati perubahan dari ViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        // Amati status loading untuk menampilkan/menyembunyikan ProgressBar
        addViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Amati hasil penambahan data
        addViewModel.addResult.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish() // Kembali ke MainActivity setelah sukses
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}