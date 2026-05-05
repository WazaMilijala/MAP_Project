package com.example.valentinesgarage.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.models.ServiceTask
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

    // Selected truck ID as LiveData
    private val _selectedTruckId = MutableLiveData<Long?>()
    val selectedTruckId: LiveData<Long?> = _selectedTruckId

    // All trucks as LiveData
    val trucks: LiveData<List<Truck>> = repository.getAllTrucks().asLiveData()

    // Tasks for selected truck as LiveData
    val tasks: LiveData<List<ServiceTask>> = _selectedTruckId.switchMap { truckId ->
        if (truckId != null) {
            repository.getTasksForTruck(truckId).asLiveData()
        } else {
            MutableLiveData(emptyList())
        }
    }

    fun selectTruck(truckId: Long) {
        _selectedTruckId.value = truckId
    }

    fun addTask(title: String, description: String, assignedToId: Long?) {
        viewModelScope.launch {
            _selectedTruckId.value?.let { truckId ->
                val task = ServiceTask(
                    truckId = truckId,
                    title = title,
                    description = description,
                    assignedToEmployeeId = assignedToId
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
}