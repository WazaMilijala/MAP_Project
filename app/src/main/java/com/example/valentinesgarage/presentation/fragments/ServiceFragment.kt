package com.example.valentinesgarage.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valentinesgarage.data.models.ServiceTask
import com.example.valentinesgarage.data.models.Truck
import com.example.valentinesgarage.data.models.TruckStatus  // ← Add this import
import com.example.valentinesgarage.databinding.FragmentServiceBinding
import com.example.valentinesgarage.presentation.adapters.ServiceTaskAdapter
import com.example.valentinesgarage.presentation.adapters.TruckListAdapter
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
        truckAdapter = TruckListAdapter { truck: Truck ->
            viewModel.selectTruck(truck.id)
        }

        taskAdapter = ServiceTaskAdapter(
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
    }

    private fun observeData() {
        viewModel.trucks.observe(viewLifecycleOwner) { trucks ->
            // Fixed: Use TruckStatus.DELIVERED
            truckAdapter.submitList(trucks.filter { it.status != TruckStatus.DELIVERED })
        }

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)
            updateEmptyState(tasks.isEmpty())
        }
    }

    private fun setupListeners() {
        binding.addTaskFab.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Service Task")
            .setMessage("Task creation coming soon")
            .setPositiveButton("Add") { _, _ ->
                Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showCompleteTaskDialog(taskId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Complete Task")
            .setMessage("Mark this task as complete?")
            .setPositiveButton("Complete") { _, _ ->
                Toast.makeText(context, "Task $taskId completed", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showTaskDetailsDialog(task: ServiceTask) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(task.title)
            .setMessage("Description: ${task.description}\nStatus: ${if (task.isCompleted) "Completed" else "Pending"}")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyStateView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.tasksRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}