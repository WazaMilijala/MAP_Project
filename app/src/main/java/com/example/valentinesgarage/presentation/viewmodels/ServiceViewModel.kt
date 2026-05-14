package com.example.valentinesgarage.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.models.ServiceTask
import com.example.valentinesgarage.data.models.TaskPriority
import com.example.valentinesgarage.data.models.Truck
import com.example.valentinesgarage.data.models.TruckStatus
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _selectedTruckId = MutableLiveData<Long?>()
    val selectedTruckId: LiveData<Long?> = _selectedTruckId

    val trucks: LiveData<List<Truck>> = repository.getAllTrucks().asLiveData()

    val employees = repository.getAllEmployees().asLiveData()

    // ✅ FIXED: Explicit type parameter for switchMap
    val tasks: LiveData<List<ServiceTask>> = _selectedTruckId.switchMap { truckId: Long? ->
        if (truckId != null) {
            repository.getTasksForTruck(truckId).asLiveData()
        } else {
            val empty: MutableLiveData<List<ServiceTask>> = MutableLiveData(emptyList())
            empty
        }
    }

    // ✅ FIXED: Simplified photos - use MutableLiveData directly
    private val _photos = MutableLiveData<List<String>>(emptyList())
    val photos: LiveData<List<String>> = _photos

    fun selectTruck(truckId: Long) {
        _selectedTruckId.value = truckId
        // Load photos when truck is selected (implement based on your photo storage)
        loadPhotosForTruck(truckId)
    }

    fun addTask(title: String, description: String, assignedToId: Long?, priority: TaskPriority) {
        viewModelScope.launch {
            _selectedTruckId.value?.let { truckId ->
                val task = ServiceTask(
                    truckId = truckId,
                    title = title,
                    description = description,
                    assignedToEmployeeId = assignedToId,
                    priority = priority
                )
                repository.insertTask(task)
            }
        }
    }

    fun completeTask(taskId: Long, employeeId: Long, notes: String) {
        viewModelScope.launch {
            repository.completeTask(taskId, employeeId, notes)
        }
    }

    fun updateTruckStatus(truckId: Long, status: TruckStatus) {
        viewModelScope.launch {
            repository.getTruckById(truckId)?.let { truck ->
                repository.updateTruck(truck.copy(status = status))
            }
        }
    }

    fun deleteTruck(truck: Truck) {
        viewModelScope.launch {
            // Delete all related service tasks
            repository.deleteTasksForTruck(truck.id)

            // Delete the truck
            repository.deleteTruck(truck)
        }
    }

    private fun loadPhotosForTruck(truckId: Long) {
        viewModelScope.launch {
            try {
                // Get photos from your photo storage implementation
                val photos = repository.getPhotosForTruckSync(truckId)
                _photos.value = photos
            } catch (e: Exception) {
                _photos.value = emptyList()
            }
        }
    }
}