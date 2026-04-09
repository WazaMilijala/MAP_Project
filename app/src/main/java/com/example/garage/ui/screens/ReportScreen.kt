package com.example.garage.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.garage.viewmodel.GarageViewModel

@Composable
fun ReportScreen(vm: GarageViewModel, onBack: () -> Unit) {
    val trucks = remember { mutableStateListOf<com.example.garage.data.Truck>() }

    LaunchedEffect(true) {
        trucks.addAll(vm.getAllTrucks())
    }

    LazyColumn {
        items(trucks) { truck ->
            Card(Modifier.padding(8.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Plate: ${truck.plateNumber}")
                    Text("Condition: ${truck.condition}")
                    Text("KM: ${truck.kilometers}")
                }
            }
        }
    }
    Button(onClick = onBack, modifier = Modifier.padding(16.dp)) {
        Text("Back")
    }
}