package com.example.valentinesgarage.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.models.Truck
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TruckCheckInViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    fun checkInTruck(
        licensePlate: String,
        model: String,
        brand: String,
        year: Int,
        ownerName: String,
        ownerPhone: String,
        kilometers: Int,
        condition: String,
        damages: String,
        notes: String,
        onSuccess: (Long) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val truck = Truck(
                    licensePlate = licensePlate,
                    model = model,
                    brand = brand,
                    year = year,
                    ownerName = ownerName,
                    ownerPhone = ownerPhone,
                    initialKilometers = kilometers,
                    initialCondition = condition,
                    initialDamages = damages,
                    additionalNotes = notes
                )
                val truckId = repository.insertTruck(truck)
                onSuccess(truckId)
            } catch (e: Exception) {
                onError(e.message ?: "Error checking in truck")
            }
        }
    }

    fun saveTruckPhoto(
        truckId: Long,
        imageUri: String,
        photoType: String
    ) {
        viewModelScope.launch {
            try {
                repository.addPhotoForTruck(truckId, imageUri)
            } catch (e: Exception) {
                // Handle error silently or add callback if needed
            }
        }
    }
}