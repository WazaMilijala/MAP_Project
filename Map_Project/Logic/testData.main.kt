


@file:Import("DataClasses/mechanic.main.kts")
@file:Import("DataClasses/Task.main.kts")
@file:Import("DataClasses/workLog.main.kts")

val mechanics = listOf(
    Mechanic("John", 1111),
    Mechanic("Peter", 2)
)


val trucks = listOf(
    Truck(
        truckID = "1",
        makeAndModel = "Truck 01",
        conditionAtCheckIn = "Good condition",
        kilometersAtCheckIn = 145000,
        WorkOnstatus = "In Progress",
        conditonAtCheckOut = null,
        kilometersAtCheckOut = 0
    ),
    Truck(
        truckID = "2",
        makeAndModel = "Truck 02",
        conditionAtCheckIn = "Needs engine check",
        kilometersAtCheckIn = 210000,
        WorkOnstatus = "In Progress",
        conditonAtCheckOut = null,
        kilometersAtCheckOut = 0
    )
)

val tasks = mutableListOf(
    Task(
        taskName = "Brake Pad Replacement",
        taskDescription = "Replace front brake pads and inspect rotors",
        taskProgress = "Completed",
        completedBy = "John",
        notes = "Rotors were in good condition.",
        TimeDateStarted = "12 April 2026, 03:40 AM",
        TimeDateCompleted = "12 April 2026, 05:30 AM"
    ),
    Task(
        taskName = "Oil Change",
        taskDescription = "Standard synthetic oil change and filter replacement",
        taskProgress = "In Progress",
        completedBy = "Peter",
        notes = "Check for leaks in the pan.",
        TimeDateStarted = "13 April 2026, 09:15 AM",
        TimeDateCompleted = null
    ),
    Task(
        taskName = "Engine Diagnostic",
        taskDescription = "Check engine light is on. Run full diagnostics.",
        taskProgress = "Pending",
        completedBy = null,
        notes = null,
        TimeDateStarted = null,
        TimeDateCompleted = null
    )
)