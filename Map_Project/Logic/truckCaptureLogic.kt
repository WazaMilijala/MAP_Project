package com.example.map_project.logic
import com.example.map_project.DataClasses.Truck
import com.example.map_project.DataClasses.Task
import com.example.map_project.DataClasses.Mechanic
import kotlin.collections.mutableListOf




class TruckCaptureLogic {


        // Store all trucks
        private val trucks = mutableListOf<Truck>()

        //  Add a new truck (Check-In)
        fun addTruck(truck: Truck) {
            trucks.add(truck)
        }

        //  Get all trucks
        fun getAllTrucks(): List<Truck> {
            return trucks
        }

        //  Find truck by number plate
        fun findTruck(numberPlate: String): Truck? {
            return trucks.find { it.NumberPlate == numberPlate }
        }

        //  Update work status
        fun updateWorkStatus(numberPlate: String, status: String) {
            val truck = findTruck(numberPlate)
            truck?.let {
                val index = trucks.indexOf(it)
                trucks[index] = it.copy(WorkOnStatus = status)
            }
        }

        //  Add task to a truck
        fun addTaskToTruck(numberPlate: String, task: Task) {
            val truck = findTruck(numberPlate)
            truck?.tasks?.add(task)
        }

        //  Mark task as completed
        fun completeTask(numberPlate: String, taskDescription: String, notes: String, mechanic: Mechanic?) {
            val truck = findTruck(numberPlate)
            val task = truck?.tasks?.find { it.taskName == taskDescription }

            task?.let {
                it.isDone = true
                it.notes = notes
                it.completedBy = mechanic
            }
        }

        //  Check-Out truck
        fun checkOutTruck(
            numberPlate: String,
            conditionAtCheckOut: String,
            kilometersAtCheckOut: Int
        ) {
            val truck = findTruck(numberPlate)
            truck?.let {
                val index = trucks.indexOf(it)
                trucks[index] = it.copy(
                    conditonAtCheckOut = conditionAtCheckOut,
                    kilometersAtCheckOut = kilometersAtCheckOut,
                    WorkOnStatus = "Completed"
                )
            }
        }

        // 🔹 Get report for a truck
        fun getTruckReport(numberPlate: String): String {
            val truck = findTruck(numberPlate)

            return if (truck != null) {
                val taskReport = truck.tasks.joinToString("\n") {
                    "Task: ${it.taskName}, Done: ${it.isDone}, By: ${it.completedBy}, Notes: ${it.notes}"
                }

                """
            Truck: ${truck.makeAndModel}
            Plate: ${truck.NumberPlate}
            
            Check-In Condition: ${truck.conditionAtCheckIn}
            KM at Check-In: ${truck.kilometersAtCheckIn}
            
            Check-Out Condition: ${truck.conditonAtCheckOut}
            KM at Check-Out: ${truck.kilometersAtCheckOut}
            
            Status: ${truck.WorkOnStatus}
            
            Tasks:
            $taskReport
            """.trimIndent()

            } else {
                "Truck not found"
            }
        }
    }
