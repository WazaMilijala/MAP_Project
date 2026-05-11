package com.example.valentinesgarage.data.database

import androidx.room.TypeConverter
import com.example.valentinesgarage.data.models.TaskPriority
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTaskPriority(priority: TaskPriority): String {
        return priority.name
    }

    @TypeConverter
    fun toTaskPriority(priority: String): TaskPriority {
        return TaskPriority.valueOf(priority)
    }
}