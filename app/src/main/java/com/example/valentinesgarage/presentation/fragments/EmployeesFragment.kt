package com.example.valentinesgarage.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valentinesgarage.data.models.Employee
import com.example.valentinesgarage.databinding.FragmentEmployeesBinding
import com.example.valentinesgarage.presentation.adapters.EmployeeAdapter
import com.example.valentinesgarage.presentation.viewmodels.EmployeesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeesFragment : Fragment() {

    private var _binding: FragmentEmployeesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EmployeesViewModel by viewModels()
    private lateinit var employeeAdapter: EmployeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupListeners()
    }

    private fun setupRecyclerView() {
        employeeAdapter = EmployeeAdapter { employee: Employee ->
            Toast.makeText(context, "Selected: ${employee.name}", Toast.LENGTH_SHORT).show()
        }

        binding.employeesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.employeesRecyclerView.adapter = employeeAdapter
    }

    private fun observeData() {
        viewModel.employees.observe(viewLifecycleOwner) { employees ->
            employeeAdapter.submitList(employees)
            updateEmptyState(employees.isEmpty())
        }
    }

    private fun setupListeners() {
        binding.addEmployeeFab.setOnClickListener {
            Toast.makeText(context, "Add employee coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyStateView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}