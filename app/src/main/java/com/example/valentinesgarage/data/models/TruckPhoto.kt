package com.example.valentinesgarage.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "truck_photos")
data class TruckPhoto(
    // Auto-generated database ID
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val truckId: Long, // Links the image to a specific truck
    val imageUri: String, // URI/path of the stored image
    val photoType: String, // Type/category of photo
    val createdAt: Long = System.currentTimeMillis()
)