//this is task data class, info on whatever task mechanics do
package com.example.map_project.DataClasses;

data class Task(
    val taskName: String,
    val taskDescription:String,
    val taskProgress: String,
    var completedBy: String?,
    var notes: String?,
    var TimeDateStarted: String?,
    var TimeDateCompleted: String?

)