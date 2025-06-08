package com.example.uap.ui.uii.update

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uap.R
import com.example.uap.databinding.ActivityAddBinding // PENTING: Kita gunakan binding dari Activity Add

class UpdateActivity : AppCompatActivity() {

    // Kita gunakan binding dari layout "tambah" karena layoutnya sama
    private lateinit var binding: ActivityAddBinding
    private val updateViewModel: UpdateViewModel by viewModels()
    private var originalPlantName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate layout activity_add.xml
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil data dari Intent dan langsung isi ke dalam form
        populateFormWithIntentData()

        // 2. Ubah teks di komponen UI sesuai konteks "Update"
        binding.toolbar.findViewById<TextView>(R.id.btnSubmit)?.text = "Update Item" // Ganti ID jika ada
        binding.btnSubmit.text = "Simpan" // Mengubah teks tombol

        // 3. Set listener untuk tombol "Simpan"
        binding.btnSubmit.setOnClickListener {
            val name = binding.etNama.text.toString().trim()
            val price = binding.etHarga.text.toString().trim()
            val description = binding.etDeskripsi.text.toString().trim()

            if (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()) {
                // Panggil ViewModel untuk melakukan update
                originalPlantName?.let {
                    updateViewModel.updatePlant(it, name, description, price)
                }
            } else {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Amati hasil proses update dari ViewModel
        observeViewModel()
    }

    private fun populateFormWithIntentData() {
        originalPlantName = intent.getStringExtra("PLANT_NAME") // DIUBAH
        if (originalPlantName == null) {
            Toast.makeText(this, "Nama Tanaman tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.etNama.setText(intent.getStringExtra("PLANT_NAME"))
        binding.etHarga.setText(intent.getStringExtra("PLANT_PRICE"))
        binding.etDeskripsi.setText(intent.getStringExtra("PLANT_DESC"))

        Glide.with(this)
            .load(intent.getStringExtra("PLANT_IMAGE_URL"))
            .placeholder(R.drawable.tanaman)
            .into(binding.ivPlantImage)
    }

    private fun observeViewModel() {
        updateViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        updateViewModel.updateResult.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish() // Tutup halaman update dan kembali ke halaman sebelumnya
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}