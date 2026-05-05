package com.example.valentinesgarage.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "trucks")
data class Truck(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val licensePlate: String,
    val model: String,
    val brand: String,
    val year: Int,
    val ownerName: String,
    val ownerPhone: String,
    val checkInDate: Date = Date(),
    var checkOutDate: Date? = null,
    var status: TruckStatus = TruckStatus.CHECKED_IN,
    var initialKilometers: Int = 0,
    var finalKilometers: Int? = null,
    var initialCondition: String = "",
    var finalCondition: String? = null,
    var initialDamages: String = "",
    var additionalNotes: String = ""
) : Parcelable

enum class TruckStatus {
    CHECKED_IN, IN_SERVICE, COMPLETED, DELIVERED
}