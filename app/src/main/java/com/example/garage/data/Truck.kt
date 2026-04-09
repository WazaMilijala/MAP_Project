package com.example.garage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Truck(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plateNumber: String,
    val condition: String,
    val kilometers: Int
)
