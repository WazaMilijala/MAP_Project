package com.example.garage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val truckId: Int,
    val description: String,
    val completed: Boolean = false,
    val employee: String
)
