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

import android.widget.ArrayAdapter
import com.example.valentinesgarage.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        employeeAdapter = EmployeeAdapter(

            // Normal tap
            onEmployeeClick = { employee ->

                Toast.makeText(
                    context,
                    employee.name,
                    Toast.LENGTH_SHORT
                ).show()
            },


            // Long press
            onEmployeeLongClick = { employee ->

                val passwordInput = android.widget.EditText(requireContext())

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Employee")
                    .setMessage("Enter admin password to delete ${employee.name}")
                    .setView(passwordInput)

                    .setPositiveButton("Delete") { _, _ ->

                        val password = passwordInput.text.toString()

                        if (password == "admin123") {

                            viewModel.deleteEmployee(employee)

                            Toast.makeText(
                                context,
                                "${employee.name} deleted",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                context,
                                "Incorrect password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        binding.employeesRecyclerView.layoutManager =
            LinearLayoutManager(context)

        binding.employeesRecyclerView.adapter =
            employeeAdapter
    }


        private fun observeData() {

            viewModel.employees.observe(viewLifecycleOwner) { employees ->

                Toast.makeText(
                    context,
                    "Employees: ${employees.size}",
                    Toast.LENGTH_SHORT
                ).show()

                employeeAdapter.submitList(employees)

                updateEmptyState(employees.isEmpty())
            }
        }


    private fun setupListeners() {

        binding.addEmployeeFab.setOnClickListener {

            showAddEmployeeDialog()
        }
    }
    private fun showAddEmployeeDialog() {

        // Password field first
        val passwordInput = android.widget.EditText(requireContext())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Admin Verification")
            .setMessage("Enter admin password")
            .setView(passwordInput)

            .setPositiveButton("Continue") { _, _ ->

                val password = passwordInput.text.toString()

                if (password != "admin123") {

                    Toast.makeText(
                        context,
                        "Incorrect password",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setPositiveButton
                }

                // Open employee form
                val dialogView = layoutInflater.inflate(
                    R.layout.dialog_add_employee,
                    null
                )

                // Inputs
                val nameInput =
                    dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                        R.id.nameInput
                    )

                val employeeIdInput =
                    dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                        R.id.employeeIdInput
                    )

                val emailInput =
                    dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                        R.id.emailInput
                    )

                val phoneInput =
                    dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                        R.id.phoneInput
                    )

                val roleInput =
                    dialogView.findViewById<android.widget.AutoCompleteTextView>(
                        R.id.roleInput
                    )

                val addButton =
                    dialogView.findViewById<com.google.android.material.button.MaterialButton>(
                        R.id.addButton
                    )

                val cancelButton =
                    dialogView.findViewById<com.google.android.material.button.MaterialButton>(
                        R.id.cancelButton
                    )

                // Role dropdown
                val roles = listOf(
                    "MECHANIC",
                    "SUPERVISOR",
                    "MANAGER",
                    "ADMIN"
                )

                roleInput.setAdapter(
                    android.widget.ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        roles
                    )
                )

                // Create dialog
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setView(dialogView)
                    .show()

                // Add employee
                addButton.setOnClickListener {

                    val name = nameInput.text.toString().trim()
                    val employeeId = employeeIdInput.text.toString().trim()
                    val email = emailInput.text.toString().trim()
                    val phone = phoneInput.text.toString().trim()
                    val roleText = roleInput.text.toString()

                    if (
                        name.isEmpty() ||
                        employeeId.isEmpty() ||
                        email.isEmpty() ||
                        phone.isEmpty() ||
                        roleText.isEmpty()
                    ) {

                        Toast.makeText(
                            context,
                            "Fill all fields",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@setOnClickListener
                    }



                    viewModel.addEmployee(
                        name = name,
                        email = email,
                        employeeId = employeeId,
                        phone = phone,
                        role = enumValueOf(roleText)
                    )

                    Toast.makeText(
                        context,
                        "Employee added",
                        Toast.LENGTH_SHORT
                    ).show()

                    dialog.dismiss()
                }

                // Cancel
                cancelButton.setOnClickListener {

                    dialog.dismiss()
                }
            }

            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {

        binding.emptyStateView.visibility =
            if (isEmpty) View.VISIBLE else View.GONE

        binding.employeesRecyclerView.visibility =
            if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}