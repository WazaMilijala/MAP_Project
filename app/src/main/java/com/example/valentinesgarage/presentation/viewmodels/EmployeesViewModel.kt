package com.example.valentinesgarage.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.models.Employee
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    val employees: LiveData<List<Employee>> = repository.getAllEmployees().asLiveData()

    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.insertEmployee(employee)
        }
    }
}