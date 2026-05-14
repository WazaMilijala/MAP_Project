package com.example.valentinesgarage.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valentinesgarage.R
import com.example.valentinesgarage.data.models.Employee
import com.example.valentinesgarage.data.models.ServiceTask
import com.example.valentinesgarage.data.models.Truck
import com.example.valentinesgarage.data.models.TruckStatus
import com.example.valentinesgarage.databinding.FragmentServiceBinding
import com.example.valentinesgarage.presentation.adapters.ServiceTaskAdapter
import com.example.valentinesgarage.presentation.adapters.TruckListAdapter
import com.example.valentinesgarage.presentation.adapters.TruckPhotoAdapter
import com.example.valentinesgarage.presentation.viewmodels.ServiceViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var truckAdapter: TruckListAdapter
    private lateinit var taskAdapter: ServiceTaskAdapter
    private lateinit var photoAdapter: TruckPhotoAdapter

    private var selectedEmployeeId: Long? = null
    private var employeeList = emptyList<Employee>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeData()
        setupListeners()
    }

    private fun setupRecyclerViews() {
        truckAdapter = TruckListAdapter(
            onTruckClick = { truck: Truck ->
                viewModel.selectTruck(truck.id)
            },
            onTruckLongClick = { truck: Truck ->
                showDeleteTruckDialog(truck)
            }
        )

        taskAdapter = ServiceTaskAdapter(
            employees = employeeList,
            onTaskChecked = { task: ServiceTask, isChecked: Boolean ->
                if (isChecked) {
                    showCompleteTaskDialog(task.id)
                }
            },
            onTaskClick = { task: ServiceTask ->
                showTaskDetailsDialog(task)
            }
        )

        binding.trucksRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.trucksRecyclerView.adapter = truckAdapter

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.tasksRecyclerView.adapter = taskAdapter

        binding.photosRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeData() {
        // ✅ FIXED: Observe trucks with explicit types
        viewModel.trucks.observe(viewLifecycleOwner) { trucks: List<Truck> ->
            truckAdapter.submitList(trucks.filter { it.status != TruckStatus.DELIVERED })
        }

        // ✅ FIXED: Observe tasks with explicit types
        viewModel.tasks.observe(viewLifecycleOwner) { tasks: List<ServiceTask> ->
            taskAdapter.submitList(tasks)
            updateEmptyState(tasks.isEmpty())
        }

        // ✅ FIXED: Observe photos with explicit types
        viewModel.photos.observe(viewLifecycleOwner) { photos: List<String> ->
            photoAdapter = TruckPhotoAdapter(photos) { imageUri: String ->
                val bundle = Bundle().apply {
                    putString("imageUri", imageUri)
                }
                findNavController().navigate(
                    R.id.fullscreenPhotoFragment,
                    bundle
                )
            }
            binding.photosRecyclerView.adapter = photoAdapter
        }

        // ✅ FIXED: Observe employees with explicit types
        viewModel.employees.observe(viewLifecycleOwner) { employees: List<Employee> ->
            employeeList = employees
            taskAdapter.updateEmployees(employees)
            taskAdapter.submitList(viewModel.tasks.value ?: emptyList())
        }
    }

    private fun setupListeners() {
        binding.addTaskFab.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialogView: View = layoutInflater.inflate(R.layout.dialogue_add_task, null)

        val titleInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.taskTitleInput)
        val descriptionInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.taskDescriptionInput)
        val assignToInput = dialogView.findViewById<android.widget.AutoCompleteTextView>(R.id.assignToInput)
        val priorityInput = dialogView.findViewById<android.widget.AutoCompleteTextView>(R.id.priorityInput)

        val priorities = listOf("LOW", "MEDIUM", "HIGH", "URGENT")
        priorityInput.setAdapter(
            android.widget.ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                priorities
            )
        )

        val addButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.addButton)
        val cancelButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.cancelButton)

        // Observe employees for dropdown
        viewModel.employees.observe(viewLifecycleOwner) { employees: List<Employee> ->
            val employeeNames = employees.map { it.name }
            val employeeAdapter = android.widget.ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                employeeNames
            )
            assignToInput.setAdapter(employeeAdapter)
            assignToInput.setOnItemClickListener { _, _, position, _ ->
                selectedEmployeeId = employees[position].id
            }
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        addButton.setOnClickListener {
            val priorityText = priorityInput.text.toString()
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()

            if (title.isEmpty()) {
                titleInput.error = "Task title required"
                return@setOnClickListener
            }

            viewModel.addTask(
                title = title,
                description = description,
                assignedToId = selectedEmployeeId,
                priority = enumValueOf(priorityText)
            )
            Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showCompleteTaskDialog(taskId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Complete Task")
            .setMessage("Mark this task as complete?")
            .setPositiveButton("Complete") { _, _ ->
                viewModel.completeTask(
                    taskId = taskId,
                    employeeId = 1L,
                    notes = "Completed from service screen"
                )
                Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showTaskDetailsDialog(task: ServiceTask) {
        val assignedEmployee = employeeList.find { it.id == task.assignedToEmployeeId }
        val assignedName = assignedEmployee?.name ?: "Unassigned"

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(task.title)
            .setMessage(
                "Description: ${task.description}\n\n" +
                        "Assigned To: $assignedName\n" +
                        "Status: ${if (task.isCompleted) "Completed" else "Pending"}"
            )
            .setPositiveButton("OK", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyStateView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.tasksRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showDeleteTruckDialog(truck: Truck) {
        val passwordInput = android.widget.EditText(requireContext())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Truck")
            .setMessage("Enter admin password to delete this truck")
            .setView(passwordInput)
            .setPositiveButton("Delete") { _, _ ->
                val password = passwordInput.text.toString()
                if (password == "admin123") {
                    Toast.makeText(context, "${truck.licensePlate} deleted", Toast.LENGTH_SHORT).show()
                    viewModel.deleteTruck(truck)
                } else {
                    Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}