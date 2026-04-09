package com.example.garage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.garage.data.GarageDatabase
import com.example.garage.repository.GarageRepository
import com.example.garage.viewmodel.GarageViewModel
import com.example.garage.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, GarageDatabase::class.java, "garage_db").build()
        val repo = GarageRepository(db.garageDao())
        val vm = GarageViewModel(repo)

        setContent {
            NavGraph(vm)
        }
    }
}
