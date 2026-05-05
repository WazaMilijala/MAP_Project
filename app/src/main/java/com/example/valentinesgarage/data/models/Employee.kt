package com.example.valentinesgarage.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val role: EmployeeRole,
    val email: String,
    val phone: String,
    val employeeId: String,
    val isActive: Boolean = true
) : Parcelable

enum class EmployeeRole {
    MECHANIC, SUPERVISOR, MANAGER, ADMIN
}