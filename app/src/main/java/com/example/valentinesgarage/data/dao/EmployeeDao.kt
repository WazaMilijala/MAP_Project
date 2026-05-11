package com.example.valentinesgarage.data.dao

import androidx.room.*
import com.example.valentinesgarage.data.models.Employee
import com.example.valentinesgarage.data.models.EmployeeRole
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY name")
    fun getAllEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE role = :role")
    fun getEmployeesByRole(role: EmployeeRole): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE id = :employeeId")
    suspend fun getEmployeeById(employeeId: Long): Employee?

    @Insert
    suspend fun insertEmployee(employee: Employee): Long

    @Update
    suspend fun updateEmployee(employee: Employee)

    @Delete
    suspend fun deleteEmployee(employee: Employee)

    @Query("SELECT * FROM employees WHERE isActive = 1")
    fun getActiveEmployees(): Flow<List<Employee>>

}