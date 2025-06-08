package com.example.uap.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uap.databinding.ActivityMainBinding
import com.example.uap.model.Plant
import com.example.uap.ui.uii.add.AddActivity
import com.example.uap.ui.uii.detail.DetailActivity
import com.example.uap.ui.uii.main.MainViewModel
import com.example.uap.ui.uii.main.PlantAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var plantAdapter: PlantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.btnTambahList.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchPlants()
    }

    private fun setupRecyclerView() {
        plantAdapter = PlantAdapter(emptyList())

        plantAdapter.onItemClick = { selectedPlant ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("PLANT_NAME", selectedPlant.plantName)
            startActivity(intent)
        }

        plantAdapter.onDeleteClick = { plantToDelete ->
            showDeleteConfirmationDialog(plantToDelete)
        }

        plantAdapter.onDetailClick = { selectedPlant ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("PLANT_NAME", selectedPlant.plantName)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = plantAdapter
        }
    }

    private fun observeViewModel() {
        mainViewModel.plantList.observe(this) { plants ->
            plantAdapter.updateData(plants)
        }

        mainViewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.deleteStatus.observe(this) { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(plant: Plant) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus tanaman '${plant.plantName}'?")
            .setPositiveButton("Ya, Hapus") { _, _ ->
                mainViewModel.deletePlant(plant.plantName)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}