package com.example.valentinesgarage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.valentinesgarage.data.models.TruckPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckPhotoDao {

    // Insert a new truck photo
    @Insert
    suspend fun insertPhoto(photo: TruckPhoto)

    // Get full TruckPhoto objects
    @Query("SELECT * FROM truck_photos WHERE truckId = :truckId")
    fun getTruckPhotos(truckId: Long): Flow<List<TruckPhoto>>

    // Get only photo paths as Strings
    @Query("SELECT imageUri FROM truck_photos WHERE truckId = :truckId")
    fun getPhotoPathsForTruck(truckId: Long): Flow<List<String>>

    @Query("DELETE FROM truck_photos WHERE truckId = :truckId")
    suspend fun deletePhotosForTruck(truckId: Long)
}