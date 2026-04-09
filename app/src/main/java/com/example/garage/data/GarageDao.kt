package com.example.garage.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GarageDao {

    @Insert
    suspend fun insertTruck(truck: Truck)

    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Truck")
    suspend fun getTrucks(): List<Truck>

    @Query("SELECT * FROM Task WHERE truckId = :truckId")
    suspend fun getTasks(truckId: Int): List<Task>
}
