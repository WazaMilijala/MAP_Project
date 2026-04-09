package com.example.garage.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Truck::class, Task::class], version = 1)
abstract class GarageDatabase : RoomDatabase() {
    abstract fun garageDao(): GarageDao
}
