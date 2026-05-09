package com.example.valentinesgarage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.valentinesgarage.data.models.TruckPhoto

@Dao
interface TruckPhotoDao {

    @Insert
    // Inserts a new photo into the database
    suspend fun insertPhoto(photo: TruckPhoto)

    @Query("SELECT * FROM truck_photos WHERE truckId = :truckId")
    // Returns all photos linked to a truck
    fun getPhotosForTruck(truckId: Long): Flow<List<TruckPhoto>>
}