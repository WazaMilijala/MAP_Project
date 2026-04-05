package com.example.map_project.logic



import com.example.map_project.DataClasses.Mechanic
import com.example.map_project.DataClasses.Task
import com.example.map_project.DataClasses.Truck

class TaskLogic {

    // Add a task to a truck
    fun addTask(truck: Truck, task: Task) {
        truck.tasks.add(task)
    }

    //  Start a task (records start time)
    fun startTask(truck: Truck, taskName: String, startTime: String) {
        val task = truck.tasks.find { it.taskName == taskName }
        task?.TimeDateStarted = startTime
    }

    //  Complete a task
    fun completeTask(
        truck: Truck,
        taskName: String,
        mechanic: Mechanic,
        notes: String,
        completionTime: String
    ) {
        val task = truck.tasks.find { it.taskName == taskName }

        task?.let {
            it.isDone = true
            it.completedBy = mechanic
            it.notes = notes
            it.TimeDateCompleted = completionTime
        }
    }

    //  Get all tasks for a truck
    fun getAllTasks(truck: Truck): List<Task> {
        return truck.tasks
    }

    //  Get only completed tasks
    fun getCompletedTasks(truck: Truck): List<Task> {
        return truck.tasks.filter { it.isDone }
    }

    //  Get pending tasks
    fun getPendingTasks(truck: Truck): List<Task> {
        return truck.tasks.filter { !it.isDone }
    }

    //  Generate a task report for a truck
    fun generateTaskReport(truck: Truck): String {
        return truck.tasks.joinToString("\n") {
            """
            Task: ${it.taskName}
            Description: ${it.taskDescription}
            Done: ${it.isDone}
            Completed By: ${it.completedBy ?: "Not yet"}
            Notes: ${it.notes ?: "No notes"}
            Started: ${it.TimeDateStarted ?: "Not started"}
            Completed: ${it.TimeDateCompleted ?: "Not completed"}
            """.trimIndent()
        }
    }
}