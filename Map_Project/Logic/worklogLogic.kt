package com.example.map_project.logic



import com.example.map_project.DataClasses.WorkLog
import com.example.map_project.DataClasses.Truck
import com.example.map_project.DataClasses.Task
import com.example.map_project.DataClasses.Mechanic
import java.time.LocalDateTime

class WorkLogLogic{

    // List to store all work logs
    private val workLogs = mutableListOf<WorkLog>()

    // Auto-increment ID tracker
    private var nextId = 1

    // Add a new work log
    fun addWorkLog(truck: Truck, task: Task, mechanic: Mechanic, notes: String? = null): WorkLog {
        // Create unique work log ID
        val id = nextId++

        val workLog = WorkLog(
            workLogId = id,
            truck = truck,
            task = task,
            mechanic = mechanic,
            notes = notes,
            timeStarted = LocalDateTime.now()
        )

        workLogs.add(workLog)
        return workLog
    }

    // Complete a work log entry
    fun completeWorkLog(workLog: WorkLog) {
        workLog.timeCompleted = LocalDateTime.now()
        workLog.task.isDone = true          // Mark task as done
        workLog.task.completedBy = workLog.mechanic
    }

    //  Get all logs
    fun getAllWorkLogs(): List<WorkLog> = workLogs

    //  Get logs for a specific truck
    fun getLogsForTruck(truck: Truck): List<WorkLog> =
        workLogs.filter { it.truck == truck }

    //  Get logs for a specific mechanic
    fun getLogsForMechanic(mechanic: Mechanic): List<WorkLog> =
        workLogs.filter { it.mechanic == mechanic }

    //  Get logs for a specific task
    fun getLogsForTask(task: Task): List<WorkLog> =
        workLogs.filter { it.task == task }

    // Generate report
    fun generateWorkLogReport(): String {
        return workLogs.joinToString("\n\n") { log ->
            """
            WorkLog ID: ${log.workLogId}
            Truck: ${log.truck.NumberPlate} (${log.truck.makeAndModel})
            Task: ${log.task.taskName}
            Mechanic: ${log.mechanic.Name}
            Notes: ${log.notes ?: "No notes"}
            Started: ${log.timeStarted}
            Completed: ${log.timeCompleted ?: "In progress"}
            """.trimIndent()
        }
    }
}