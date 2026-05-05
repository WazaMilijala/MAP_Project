package com.example.valentinesgarage.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _employeeProductivity = MutableLiveData<Map<String, Int>>()
    val employeeProductivity: LiveData<Map<String, Int>> = _employeeProductivity

    private val _vehicleConditionReport = MutableLiveData<List<Pair<String, Int>>>()
    val vehicleConditionReport: LiveData<List<Pair<String, Int>>> = _vehicleConditionReport

    private val _totalTrucks = MutableLiveData<Int>(0)
    val totalTrucks: LiveData<Int> = _totalTrucks

    private val _completedTasks = MutableLiveData<Int>(0)
    val completedTasks: LiveData<Int> = _completedTasks

    fun generateReport() {
        viewModelScope.launch {
            val startDate = System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000
            val endDate = System.currentTimeMillis()

            try {
                val trucks = repository.getTrucksForReport(startDate, endDate)

                _totalTrucks.postValue(trucks.size)

                val conditionStats = trucks.groupBy { it.initialCondition }
                    .mapValues { it.value.size }

                _vehicleConditionReport.postValue(conditionStats.toList())

                // Sample productivity data
                _employeeProductivity.postValue(mapOf(
                    "John Doe" to 15,
                    "Jane Smith" to 12,
                    "Mike Johnson" to 8
                ))

                _completedTasks.postValue(35)
            } catch (e: Exception) {
                _totalTrucks.postValue(0)
                _completedTasks.postValue(0)
                _vehicleConditionReport.postValue(emptyList())
                _employeeProductivity.postValue(emptyMap())
            }
        }
    }

    fun exportReport() {
        viewModelScope.launch {
            // Export implementation
        }
    }
}