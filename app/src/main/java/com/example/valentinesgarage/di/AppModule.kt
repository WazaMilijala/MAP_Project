package com.example.valentinesgarage.di

import android.content.Context
import com.example.valentinesgarage.data.database.GarageDatabase
import com.example.valentinesgarage.data.dao.*
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGarageDatabase(@ApplicationContext context: Context): GarageDatabase {
        return GarageDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTruckDao(database: GarageDatabase): TruckDao = database.truckDao()

    @Provides
    @Singleton
    fun provideEmployeeDao(database: GarageDatabase): EmployeeDao = database.employeeDao()

    @Provides
    @Singleton
    fun provideServiceTaskDao(database: GarageDatabase): ServiceTaskDao = database.serviceTaskDao()

    @Provides
    @Singleton
    fun provideTruckPhotoDao(database: GarageDatabase): TruckPhotoDao {
        return database.truckPhotoDao()
    }
}