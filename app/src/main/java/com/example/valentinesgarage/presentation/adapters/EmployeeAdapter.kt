package com.example.valentinesgarage.presentation.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valentinesgarage.data.models.Employee
import com.example.valentinesgarage.databinding.ItemEmployeeBinding

class EmployeeAdapter(
    private val onEmployeeClick: (Employee) -> Unit
) : ListAdapter<Employee, EmployeeAdapter.EmployeeViewHolder>(EmployeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemEmployeeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmployeeViewHolder(binding, onEmployeeClick)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EmployeeViewHolder(
        private val binding: ItemEmployeeBinding,
        private val onEmployeeClick: (Employee) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(employee: Employee) {
            binding.apply {
                employeeNameText.text = employee.name
                employeeRoleText.text = employee.role.name
                employeeIdText.text = employee.employeeId

                root.setOnClickListener {
                    onEmployeeClick(employee)
                }
            }
        }
    }

    class EmployeeDiffCallback : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }
}