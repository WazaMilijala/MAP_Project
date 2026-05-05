package com.example.valentinesgarage.utils


import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getCurrentDate(): Date = Calendar.getInstance().time

    fun formatDate(date: Date, pattern: String = "dd/MM/yyyy"): String {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }

    fun getDateRange(days: Int): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val startDate = calendar.timeInMillis
        return Pair(startDate, endDate)
    }
}