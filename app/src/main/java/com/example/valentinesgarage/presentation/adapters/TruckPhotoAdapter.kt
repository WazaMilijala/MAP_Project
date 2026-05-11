package com.example.valentinesgarage.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valentinesgarage.databinding.ItemTruckPhotoBinding

class TruckPhotoAdapter(
    private val photos: List<String>,
    private val onPhotoClick: (String) -> Unit
) : RecyclerView.Adapter<TruckPhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(
        private val binding: ItemTruckPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photoUri: String) {
            binding.root.setOnClickListener {
                onPhotoClick(photoUri)
            }

            Glide.with(binding.root.context)
                .load(Uri.parse(photoUri))
                .into(binding.photoImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {

        val binding = ItemTruckPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PhotoViewHolder,
        position: Int
    ) {

        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size
}