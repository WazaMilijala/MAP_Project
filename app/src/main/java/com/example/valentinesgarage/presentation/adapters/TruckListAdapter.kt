package com.example.valentinesgarage.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valentinesgarage.data.models.Truck
import com.example.valentinesgarage.databinding.ItemTruckBinding

class TruckListAdapter(
    private val onTruckClick: (Truck) -> Unit
) : ListAdapter<Truck, TruckListAdapter.TruckViewHolder>(TruckDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val binding = ItemTruckBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TruckViewHolder(binding, onTruckClick)
    }

    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TruckViewHolder(
        private val binding: ItemTruckBinding,
        private val onTruckClick: (Truck) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(truck: Truck) {
            binding.apply {
                licensePlateText.text = truck.licensePlate
                modelText.text = "${truck.brand} ${truck.model}"
                statusText.text = truck.status.name
                kilometerText.text = "${truck.initialKilometers} km"

                root.setOnClickListener {
                    onTruckClick(truck)
                }
            }
        }
    }

    class TruckDiffCallback : DiffUtil.ItemCallback<Truck>() {
        override fun areItemsTheSame(oldItem: Truck, newItem: Truck): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Truck, newItem: Truck): Boolean {
            return oldItem == newItem
        }
    }
}