package com.example.uap.ui.uii.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uap.R
import com.example.uap.databinding.ActivityDetailBinding
import com.example.uap.model.Plant
import com.example.uap.ui.uii.update.UpdateActivity
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var currentPlant: Plant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plantName = intent.getStringExtra("PLANT_NAME")

        if (!plantName.isNullOrEmpty()) {
            detailViewModel.fetchPlantDetail(plantName)
        } else {
            Toast.makeText(this, "Nama Tanaman tidak valid", Toast.LENGTH_SHORT).show()
            finish()
        }

        observeViewModel()

        binding.btnUpdate.setOnClickListener {
            // Di dalam DetailActivity.kt, listener untuk btnUpdate
            currentPlant?.let { plant ->
                val intent = Intent(this, UpdateActivity::class.java).apply {
                    // DIUBAH: Tidak perlu kirim ID, nama sudah jadi kunci utama
                    putExtra("PLANT_NAME", plant.plantName)
                    putExtra("PLANT_PRICE", plant.price)
                    putExtra("PLANT_DESC", plant.description)
                    putExtra("PLANT_IMAGE_URL", plant.imageUrl)
                }
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {

        detailViewModel.plantDetail.observe(this) { plant ->
            currentPlant = plant
            populateUi(plant)
        }

        detailViewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateUi(plant: Plant) {
        binding.tvDetailName.text = plant.plantName
        binding.tvDetailDescription.text = plant.description

        try {
            val priceLong = plant.price.toLong()
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            binding.tvDetailPrice.text = format.format(priceLong)
        } catch (e: NumberFormatException) {
            binding.tvDetailPrice.text = plant.price
        }

        Glide.with(this)
            .load(plant.imageUrl)
            .placeholder(R.drawable.tanaman)
            .into(binding.ivDetailImage)
    }
}