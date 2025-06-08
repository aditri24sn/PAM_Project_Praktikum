package com.example.uap.ui.uii.main
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uap.R
import com.example.uap.databinding.ItemPlantBinding
import com.example.uap.model.Plant
import java.text.NumberFormat
import java.util.Locale

class PlantAdapter(private var plantList: List<Plant>) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {
    var onItemClick: ((Plant) -> Unit)? = null
    var onDeleteClick: ((Plant) -> Unit)? = null
    var onDetailClick: ((Plant) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantList[position]
        holder.bind(plant, onItemClick, onDeleteClick, onDetailClick)
    }

    override fun getItemCount(): Int = plantList.size

    fun updateData(newPlantList: List<Plant>?) {
        this.plantList = newPlantList ?: emptyList()
        notifyDataSetChanged()
        Log.d("AdapterUpdate", "Adapter diupdate dengan ${this.plantList.size} item.")
    }

    class PlantViewHolder(private val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            plant: Plant,
            onItemClick: ((Plant) -> Unit)?,
            onDeleteClick: ((Plant) -> Unit)?,
            onDetailClick: ((Plant) -> Unit)? // Tambahkan parameter ini
        ) {
            Log.d("AdapterBinding", "Binding data untuk: ${plant.plantName}")

            binding.tvPlantName.text = plant.plantName

            try {
                val priceLong = plant.price.toLong()
                val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                binding.tvPlantPrice.text = format.format(priceLong)
            } catch (e: NumberFormatException) {
                binding.tvPlantPrice.text = plant.price
            }

            Glide.with(itemView.context)
                .load(plant.imageUrl)
                .placeholder(R.drawable.tanaman)
                .error(R.drawable.tanaman)
                .into(binding.ivPlantImage)

            itemView.setOnClickListener {
                onItemClick?.invoke(plant)
            }
            binding.btnHapus.setOnClickListener {
                onDeleteClick?.invoke(plant)
            }
            binding.btnDetail.setOnClickListener {
                onDetailClick?.invoke(plant)
            }
        }
    }
}