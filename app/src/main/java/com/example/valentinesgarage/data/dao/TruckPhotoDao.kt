package com.example.valentinesgarage.data.dao

import androidx.room.*
import com.example.valentinesgarage.data.models.TruckPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckPhotoDao {
    @Query("SELECT * FROM truck_photos WHERE truckId = :truckId ORDER BY createdAt DESC")
    fun getPhotosForTruck(truckId: Long): Flow<List<TruckPhoto>>

    @Insert
    suspend fun insertPhoto(photo: TruckPhoto): Long

    @Delete
    suspend fun deletePhoto(photo: TruckPhoto)

    @Query("DELETE FROM truck_photos WHERE truckId = :truckId")
    suspend fun deletePhotosForTruck(truckId: Long)
}