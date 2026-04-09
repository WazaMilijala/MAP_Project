package com.example.garage.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.garage.viewmodel.GarageViewModel

@Composable
fun CheckInScreen(vm: GarageViewModel, onNext: () -> Unit) {
    var plate by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var km by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Truck Check-In", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(plate, { plate = it }, label = { Text("Plate Number") })
        OutlinedTextField(condition, { condition = it }, label = { Text("Condition") })
        OutlinedTextField(km, { km = it }, label = { Text("Kilometers") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (plate.isNotEmpty() && km.isNotEmpty()) {
                vm.addTruck(plate, condition, km.toInt())
                onNext()
            }

        }, modifier = Modifier.fillMaxWidth()) {
            Text("Next → Tasks")
        }
    }
}
