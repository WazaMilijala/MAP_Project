package com.example.garage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garage.data.Task
import com.example.garage.data.Truck
import com.example.garage.repository.GarageRepository
import kotlinx.coroutines.launch

class GarageViewModel(private val repo: GarageRepository) : ViewModel() {

    fun addTruck(plate: String, condition: String, km: Int) {
        viewModelScope.launch {
            repo.addTruck(Truck(plateNumber = plate, condition = condition, kilometers = km))
        }
    }

    fun addTask(truckId: Int, description: String, employee: String) {
        viewModelScope.launch {
            repo.addTask(Task(truckId = truckId, description = description, employee = employee))
        }
    }

    suspend fun getAllTrucks(): List<Truck> {
        return repo.getTrucks()
    }
}
