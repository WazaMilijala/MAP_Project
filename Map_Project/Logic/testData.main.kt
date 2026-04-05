package com.example.map_project.logic

import com.example.map_project.DataClasses.Mechanic
import com.example.map_project.DataClasses.Task
import com.example.map_project.DataClasses.Truck

// Mechanics
val mechanics = listOf(
    Mechanic("John", 1111),
    Mechanic("Peter", 2)
)

// Trucks
val trucks = listOf(
    Truck(
        NumberPlate = "1",
        makeAndModel = "Truck 01",
        conditionAtCheckIn = "Good condition",
        kilometersAtCheckIn = 145000,
        WorkOnStatus = "In Progress",
        conditionAtCheckOut = null,
        kilometersAtCheckOut = 78
    ),

    Truck(
        NumberPlate = "2",
        makeAndModel = "Truck 02",
        conditionAtCheckIn = "Needs engine check",
        kilometersAtCheckIn = 210000,
        WorkOnStatus = "In Progress",
        conditionAtCheckOut = null,
        kilometersAtCheckOut = 0
    )
)

// Tasks
val tasks = mutableListOf(
    Task(
        taskName = "Brake Pad Replacement",
        taskDescription = "Replace front brake pads and inspect rotors",
        isDone = true,
        completedBy = mechanics[0],   // John
        notes = "Rotors were in good condition.",
        TimeDateStarted = "12 April 2026, 03:40 AM",
        TimeDateCompleted = "12 April 2026, 05:30 AM"
    ),
    Task(
        taskName = "Oil Change",
        taskDescription = "Standard synthetic oil change and filter replacement",
        isDone = false,
        completedBy = mechanics[1],   // Peter
        notes = "Check for leaks in the pan.",
        TimeDateStarted = "13 April 2026, 09:15 AM",
        TimeDateCompleted = null
    ),
    Task(
        taskName = "Engine Diagnostic",
        taskDescription = "Check engine light is on. Run full diagnostics.",
        isDone = false,
        completedBy = null,
        notes = null,
        TimeDateStarted = null,
        TimeDateCompleted = null
    )
)