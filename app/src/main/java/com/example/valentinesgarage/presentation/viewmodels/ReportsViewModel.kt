package com.example.valentinesgarage.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.models.Employee
import com.example.valentinesgarage.data.models.Truck
import com.example.valentinesgarage.data.repository.GarageRepository
import com.example.valentinesgarage.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _totalTrucks = MutableLiveData<Int>(0)
    val totalTrucks: LiveData<Int> = _totalTrucks

    private val _completedTasks = MutableLiveData<Int>(0)
    val completedTasks: LiveData<Int> = _completedTasks

    private val _pendingTasks = MutableLiveData<Int>(0)
    val pendingTasks: LiveData<Int> = _pendingTasks

    private val _averageServiceTime = MutableLiveData<String>("0h")
    val averageServiceTime: LiveData<String> = _averageServiceTime

    private val _vehicleConditionReport = MutableLiveData<List<Pair<String, Int>>>()
    val vehicleConditionReport: LiveData<List<Pair<String, Int>>> = _vehicleConditionReport

    private val _employeeProductivity = MutableLiveData<Map<String, Int>>()
    val employeeProductivity: LiveData<Map<String, Int>> = _employeeProductivity

    private val _truckStatusReport = MutableLiveData<List<Pair<String, Int>>>()
    val truckStatusReport: LiveData<List<Pair<String, Int>>> = _truckStatusReport

    private val _recentTrucks = MutableLiveData<List<String>>()
    val recentTrucks: LiveData<List<String>> = _recentTrucks

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _reportDateRange = MutableLiveData<String>("Last 30 Days")
    val reportDateRange: LiveData<String> = _reportDateRange

    fun generateReport() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val startDate = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
                val endDate = System.currentTimeMillis()

                // Get trucks from database
                val trucks = repository.getTrucksForReport(startDate, endDate)

                // Generate summary statistics
                generateSummaryStats(trucks, startDate, endDate)

                // Generate vehicle condition report
                generateVehicleConditionReport(trucks)

                // Generate truck status distribution
                generateTruckStatusReport(trucks)

                // Generate recent activity
                generateRecentActivity(trucks)

                // Generate employee productivity
                generateEmployeeProductivityReport(startDate, endDate)

                // Update date range display
                _reportDateRange.value = "${DateUtils.formatDate(Date(startDate))} - ${DateUtils.formatDate(Date(endDate))}"

            } catch (e: Exception) {
                e.printStackTrace()
                setDefaultValues()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun generateSummaryStats(trucks: List<Truck>, startDate: Long, endDate: Long) {
        _totalTrucks.value = trucks.size

        var completedCount = 0
        var pendingCount = 0

        for (truck in trucks) {
            try {
                val tasks = repository.getTasksForTruckSync(truck.id)
                completedCount += tasks.count { it.isCompleted }
                pendingCount += tasks.count { !it.isCompleted }
            } catch (e: Exception) {
                // Ignore errors for individual trucks
            }
        }

        _completedTasks.value = completedCount
        _pendingTasks.value = pendingCount

        // Calculate average service time
        val completedTrucks = trucks.filter {
            it.status == com.example.valentinesgarage.data.models.TruckStatus.COMPLETED ||
                    it.status == com.example.valentinesgarage.data.models.TruckStatus.DELIVERED
        }

        if (completedTrucks.isNotEmpty()) {
            val totalServiceTime = completedTrucks.sumOf { truck ->
                val checkIn = truck.checkInDate.time
                val checkOut = truck.checkOutDate?.time ?: System.currentTimeMillis()
                checkOut - checkIn
            }
            val avgTimeMs = totalServiceTime / completedTrucks.size
            val avgHours = avgTimeMs / (1000 * 60 * 60)

            _averageServiceTime.value = when {
                avgHours >= 24 -> "${avgHours / 24}d ${avgHours % 24}h"
                avgHours > 0 -> "${avgHours}h"
                else -> "< 1h"
            }
        } else {
            _averageServiceTime.value = "N/A"
        }
    }

    private fun generateVehicleConditionReport(trucks: List<Truck>) {
        val conditionStats = trucks
            .filter { it.initialCondition.isNotEmpty() }
            .groupBy { it.initialCondition }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }

        _vehicleConditionReport.value = if (conditionStats.isEmpty()) {
            listOf("No data" to 0)
        } else {
            conditionStats
        }
    }

    private fun generateTruckStatusReport(trucks: List<Truck>) {
        val statusStats = trucks
            .groupBy { it.status.name }
            .mapValues { it.value.size }
            .toList()

        _truckStatusReport.value = if (statusStats.isEmpty()) {
            listOf("No data" to 0)
        } else {
            statusStats
        }
    }

    private fun generateRecentActivity(trucks: List<Truck>) {
        val recentActivity = trucks
            .sortedByDescending { it.checkInDate }
            .take(5)
            .map { truck ->
                "${truck.licensePlate} - ${truck.brand} ${truck.model} (${truck.status.name})"
            }

        _recentTrucks.value = if (recentActivity.isEmpty()) {
            listOf("No recent activity")
        } else {
            recentActivity
        }
    }

    private suspend fun generateEmployeeProductivityReport(startDate: Long, endDate: Long) {
        try {
            val employees = repository.getAllEmployeesSync()
            val productivityMap = mutableMapOf<String, Int>()

            for (employee in employees) {
                try {
                    val tasks = repository.getTasksByEmployeeSync(employee.id)
                    val completedInRange = tasks.count { task ->
                        task.completedAt?.time?.let { completedTime ->
                            completedTime in startDate..endDate
                        } ?: false
                    }
                    if (completedInRange > 0) {
                        productivityMap[employee.name] = completedInRange
                    }
                } catch (e: Exception) {
                    // Skip this employee
                }
            }

            _employeeProductivity.value = if (productivityMap.isEmpty()) {
                mapOf("No completed tasks" to 0)
            } else {
                productivityMap.toList()
                    .sortedByDescending { it.second }
                    .toMap()
            }
        } catch (e: Exception) {
            _employeeProductivity.value = mapOf("No data available" to 0)
        }
    }

    private fun setDefaultValues() {
        _totalTrucks.value = 0
        _completedTasks.value = 0
        _pendingTasks.value = 0
        _averageServiceTime.value = "0h"
        _vehicleConditionReport.value = listOf("No data" to 0)
        _employeeProductivity.value = mapOf("No data" to 0)
        _truckStatusReport.value = listOf("No data" to 0)
        _recentTrucks.value = listOf("No recent activity")
    }
}