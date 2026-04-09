package com.example.garage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.garage.viewmodel.GarageViewModel
import com.example.garage.ui.screens.*

@Composable
fun NavGraph(vm: GarageViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "checkin") {
        composable("checkin") {
            CheckInScreen(vm) { navController.navigate("tasks") }
        }
        composable("tasks") {
            TaskScreen(vm) { navController.navigate("report") }
        }
        composable("report") {
            ReportScreen(vm) { navController.navigate("checkin") }
        }
    }
}