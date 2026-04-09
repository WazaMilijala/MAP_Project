package com.example.garage.repository

import com.example.garage.data.GarageDao
import com.example.garage.data.Task
import com.example.garage.data.Truck

class GarageRepository(private val dao: GarageDao) {

    suspend fun addTruck(truck: Truck) = dao.insertTruck(truck)

    suspend fun addTask(task: Task) = dao.insertTask(task)

    suspend fun getTrucks(): List<Truck> = dao.getTrucks()

    suspend fun getTasks(truckId: Int): List<Task> = dao.getTasks(truckId)
}