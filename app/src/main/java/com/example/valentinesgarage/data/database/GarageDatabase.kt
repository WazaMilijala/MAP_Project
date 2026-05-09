package com.example.valentinesgarage.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.valentinesgarage.data.dao.*
import com.example.valentinesgarage.data.models.*

@Database(
    entities = [
        Truck::class,
        Employee::class,
        ServiceTask::class,
        TruckPhoto::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GarageDatabase : RoomDatabase() {
    abstract fun truckDao(): TruckDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun serviceTaskDao(): ServiceTaskDao

    abstract fun truckPhotoDao(): TruckPhotoDao

    companion object {
        @Volatile
        private var INSTANCE: GarageDatabase? = null

        fun getDatabase(context: Context): GarageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GarageDatabase::class.java,
                    "garage_database"
                )
                    .fallbackToDestructiveMigration() // Add this for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}