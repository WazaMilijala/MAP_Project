package com.example.valentinesgarage.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valentinesgarage.R
import com.example.valentinesgarage.data.models.Employee
import com.example.valentinesgarage.data.models.ServiceTask
import com.example.valentinesgarage.databinding.ItemServiceTaskBinding

class ServiceTaskAdapter(

    // List of employees used to match IDs to names
    private var employees: List<Employee>,

    // Callback when checkbox is checked/unchecked
    private val onTaskChecked: (ServiceTask, Boolean) -> Unit,

    // Callback when task card is tapped
    private val onTaskClick: (ServiceTask) -> Unit

) : ListAdapter<ServiceTask, ServiceTaskAdapter.TaskViewHolder>(
    TaskDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        // Inflate task card layout
        val binding = ItemServiceTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // Create view holder
        return TaskViewHolder(
            binding,
            employees,
            onTaskChecked,
            onTaskClick
        )
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {

        // Bind task data to card
        holder.bind(getItem(position))
    }

    class TaskViewHolder(

        // ViewBinding for task item layout
        private val binding: ItemServiceTaskBinding,

        // Employee list for assignment lookup
        private val employees: List<Employee>,

        // Checkbox callback
        private val onTaskChecked: (ServiceTask, Boolean) -> Unit,

        // Card click callback
        private val onTaskClick: (ServiceTask) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: ServiceTask) {
            binding.taskPriorityChip.text = task.priority.name

            // Find employee assigned to this task
            val assignedEmployee = employees.find {
                it.id == task.assignedToEmployeeId
            }

            // Show employee name or fallback text
            val assignedName =
                assignedEmployee?.name ?: "Unassigned"

            binding.apply {

                // Task title
                taskTitleText.text = task.title

                // Task description
                taskDescriptionText.text = task.description

                // Display assigned employee name
                assignedToText.text =
                    binding.root.context.getString(
                        R.string.assigned_to,
                        assignedName
                    )

                // Task completion checkbox
                taskCheckbox.isChecked = task.isCompleted

                // Handle checkbox changes
                taskCheckbox.setOnCheckedChangeListener { _, isChecked ->

                    onTaskChecked(task, isChecked)
                }

                // Open task details when card tapped
                root.setOnClickListener {

                    onTaskClick(task)
                }
            }
        }
    }

    // Optimizes RecyclerView updates
    class TaskDiffCallback : DiffUtil.ItemCallback<ServiceTask>() {

        // Checks if two items are the same database item
        override fun areItemsTheSame(
            oldItem: ServiceTask,
            newItem: ServiceTask
        ): Boolean {

            return oldItem.id == newItem.id
        }

        // Checks if contents changed
        override fun areContentsTheSame(
            oldItem: ServiceTask,
            newItem: ServiceTask
        ): Boolean {

            return oldItem == newItem
        }
    }

    fun updateEmployees(employees: List<Employee>) {
        this.employees = employees
    }
}