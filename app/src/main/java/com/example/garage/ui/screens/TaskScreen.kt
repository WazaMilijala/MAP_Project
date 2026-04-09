package com.example.garage.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.garage.viewmodel.GarageViewModel

@Composable
fun TaskScreen(vm: GarageViewModel, onNext: () -> Unit) {
    var task by remember { mutableStateOf("") }
    var employee by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Assign Tasks", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(task, { task = it }, label = { Text("Task Description") })
        OutlinedTextField(employee, { employee = it }, label = { Text("Employee Name") })

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            vm.addTask(1, task, employee)
            task = ""
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Task")
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("View Report →")
        }
    }
}
