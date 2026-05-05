package com.example.valentinesgarage.data.repository

import com.example.valentinesgarage.data.dao.*
import com.example.valentinesgarage.data.models.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GarageRepository @Inject constructor(
    private val truckDao: TruckDao,
    private val employeeDao: EmployeeDao,
    private val serviceTaskDao: ServiceTaskDao
) {
    // Truck operations
    fun getAllTrucks(): Flow<List<Truck>> = truckDao.getAllTrucks()

    fun getTrucksByStatus(status: TruckStatus): Flow<List<Truck>> =
        truckDao.getTrucksByStatus(status)

    suspend fun getTruckById(id: Long): Truck? = truckDao.getTruckById(id)

    suspend fun insertTruck(truck: Truck): Long = truckDao.insertTruck(truck)

    suspend fun updateTruck(truck: Truck) = truckDao.updateTruck(truck)

    suspend fun getTrucksForReport(startDate: Long, endDate: Long): List<Truck> =
        truckDao.getTrucksBetweenDates(startDate, endDate)

    // Employee operations
    fun getAllEmployees(): Flow<List<Employee>> = employeeDao.getAllEmployees()

    suspend fun getEmployeeById(id: Long): Employee? = employeeDao.getEmployeeById(id)

    suspend fun insertEmployee(employee: Employee): Long = employeeDao.insertEmployee(employee)

    // Task operations
    fun getTasksForTruck(truckId: Long): Flow<List<ServiceTask>> =
        serviceTaskDao.getTasksForTruck(truckId)

    fun getTasksByEmployee(employeeId: Long): Flow<List<ServiceTask>> =
        serviceTaskDao.getTasksByEmployee(employeeId)

    suspend fun insertTask(task: ServiceTask): Long = serviceTaskDao.insertTask(task)

    suspend fun updateTask(task: ServiceTask) = serviceTaskDao.updateTask(task)

    suspend fun completeTask(taskId: Long, employeeId: Long, notes: String) {
        serviceTaskDao.completeTask(
            taskId = taskId,
            employeeId = employeeId,
            completedAt = System.currentTimeMillis(),
            notes = notes
        )
    }
}