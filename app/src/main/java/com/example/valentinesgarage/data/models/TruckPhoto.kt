package com.example.valentinesgarage.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "truck_photos")
data class TruckPhoto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val truckId: Long,
    val imageUri: String,
    val photoType: String = "general", // "exterior", "interior", "damage", etc.
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable