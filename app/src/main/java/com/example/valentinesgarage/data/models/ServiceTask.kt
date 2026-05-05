package com.example.valentinesgarage.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "service_tasks")
data class ServiceTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val truckId: Long,
    val title: String,
    val description: String,
    var isCompleted: Boolean = false,
    val assignedToEmployeeId: Long? = null,
    val completedByEmployeeId: Long? = null,
    val createdAt: Date = Date(),
    var completedAt: Date? = null,
    var mechanicNotes: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM
) : Parcelable

enum class TaskPriority {
    LOW, MEDIUM, HIGH, URGENT
}