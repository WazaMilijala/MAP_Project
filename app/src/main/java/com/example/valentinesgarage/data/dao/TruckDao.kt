package com.example.valentinesgarage.data.dao

import androidx.room.*
import com.example.valentinesgarage.data.models.Truck
import com.example.valentinesgarage.data.models.TruckStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {
    @Query("SELECT * FROM trucks ORDER BY checkInDate DESC")
    fun getAllTrucks(): Flow<List<Truck>>

    @Query("SELECT * FROM trucks WHERE status = :status ORDER BY checkInDate DESC")
    fun getTrucksByStatus(status: TruckStatus): Flow<List<Truck>>

    @Query("SELECT * FROM trucks WHERE id = :truckId")
    suspend fun getTruckById(truckId: Long): Truck?

    @Insert
    suspend fun insertTruck(truck: Truck): Long

    @Update
    suspend fun updateTruck(truck: Truck)

    @Delete
    suspend fun deleteTruck(truck: Truck)

    @Query("SELECT * FROM trucks WHERE checkInDate BETWEEN :startDate AND :endDate")
    suspend fun getTrucksBetweenDates(startDate: Long, endDate: Long): List<Truck>
}