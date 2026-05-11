package com.example.valentinesgarage.data.dao

import androidx.room.*
import com.example.valentinesgarage.data.models.ServiceTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceTaskDao {
    @Query("SELECT * FROM service_tasks WHERE truckId = :truckId ORDER BY createdAt DESC")
    fun getTasksForTruck(truckId: Long): Flow<List<ServiceTask>>

    @Query("SELECT * FROM service_tasks WHERE completedByEmployeeId = :employeeId ORDER BY completedAt DESC")
    fun getTasksByEmployee(employeeId: Long): Flow<List<ServiceTask>>

    @Query("SELECT * FROM service_tasks WHERE assignedToEmployeeId = :employeeId AND isCompleted = 0")
    fun getPendingTasksForEmployee(employeeId: Long): Flow<List<ServiceTask>>

    @Insert
    suspend fun insertTask(task: ServiceTask): Long

    @Update
    suspend fun updateTask(task: ServiceTask)

    @Query("UPDATE service_tasks SET isCompleted = 1, completedAt = :completedAt, completedByEmployeeId = :employeeId, mechanicNotes = :notes WHERE id = :taskId")
    suspend fun completeTask(taskId: Long, employeeId: Long, completedAt: Long, notes: String)

    @Query("DELETE FROM service_tasks WHERE truckId = :truckId")
    suspend fun deleteTasksForTruck(truckId: Long)
}