package com.example.valentinesgarage.data.repository

import com.example.valentinesgarage.data.dao.*
import com.example.valentinesgarage.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GarageRepository @Inject constructor(
    private val truckDao: TruckDao,
    private val employeeDao: EmployeeDao,
    private val serviceTaskDao: ServiceTaskDao
) {
    // ==================== TRUCK OPERATIONS ====================

    fun getAllTrucks(): Flow<List<Truck>> = truckDao.getAllTrucks()

    fun getTrucksByStatus(status: TruckStatus): Flow<List<Truck>> = truckDao.getTrucksByStatus(status)

    suspend fun getTruckById(id: Long): Truck? = truckDao.getTruckById(id)

    suspend fun insertTruck(truck: Truck): Long = truckDao.insertTruck(truck)

    suspend fun updateTruck(truck: Truck) = truckDao.updateTruck(truck)

    suspend fun deleteTruck(truck: Truck) = truckDao.deleteTruck(truck)

    suspend fun getTrucksForReport(startDate: Long, endDate: Long): List<Truck> =
        truckDao.getTrucksBetweenDates(startDate, endDate)

    // ==================== EMPLOYEE OPERATIONS ====================

    fun getAllEmployees(): Flow<List<Employee>> = employeeDao.getAllEmployees()

    suspend fun getEmployeeById(id: Long): Employee? = employeeDao.getEmployeeById(id)

    suspend fun insertEmployee(employee: Employee): Long = employeeDao.insertEmployee(employee)

    suspend fun updateEmployee(employee: Employee) = employeeDao.updateEmployee(employee)

    suspend fun deleteEmployee(employee: Employee) = employeeDao.deleteEmployee(employee)

    suspend fun getAllEmployeesSync(): List<Employee> {
        return try {
            employeeDao.getAllEmployees().first()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ==================== TASK OPERATIONS ====================

    fun getTasksForTruck(truckId: Long): Flow<List<ServiceTask>> = serviceTaskDao.getTasksForTruck(truckId)

    fun getTasksByEmployee(employeeId: Long): Flow<List<ServiceTask>> = serviceTaskDao.getTasksByEmployee(employeeId)

    suspend fun insertTask(task: ServiceTask): Long = serviceTaskDao.insertTask(task)

    suspend fun updateTask(task: ServiceTask) = serviceTaskDao.updateTask(task)

    suspend fun completeTask(taskId: Long, employeeId: Long, notes: String) {
        serviceTaskDao.completeTask(taskId, employeeId, System.currentTimeMillis(), notes)
    }

    suspend fun deleteTasksForTruck(truckId: Long) {
        val tasks = getTasksForTruckSync(truckId)
        tasks.forEach { task ->
            serviceTaskDao.deleteTask(task)
        }
    }

    suspend fun getTasksForTruckSync(truckId: Long): List<ServiceTask> {
        return try {
            serviceTaskDao.getTasksForTruck(truckId).first()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getTasksByEmployeeSync(employeeId: Long): List<ServiceTask> {
        return try {
            serviceTaskDao.getTasksByEmployee(employeeId).first()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ==================== PHOTO OPERATIONS ====================

    // In-memory photo storage (replace with Room entity for persistent storage)
    private val photoStorage = mutableMapOf<Long, MutableList<String>>()

    /**
     * Add a photo URI for a specific truck
     */
    suspend fun addPhotoForTruck(truckId: Long, imageUri: String) {
        val photos = photoStorage.getOrPut(truckId) { mutableListOf() }
        photos.add(imageUri)
    }

    /**
     * Get all photos for a specific truck
     */
    suspend fun getPhotosForTruckSync(truckId: Long): List<String> {
        return photoStorage[truckId] ?: emptyList()
    }

    /**
     * Delete all photos for a truck
     */
    suspend fun deletePhotosForTruck(truckId: Long) {
        photoStorage.remove(truckId)
    }

    /**
     * Get photo count for a truck
     */
    suspend fun getPhotoCount(truckId: Long): Int {
        return photoStorage[truckId]?.size ?: 0
    }
}