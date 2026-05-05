package com.example.valentinesgarage.presentation.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valentinesgarage.data.models.ServiceTask
import com.example.valentinesgarage.databinding.ItemServiceTaskBinding

class ServiceTaskAdapter(
    private val onTaskChecked: (ServiceTask, Boolean) -> Unit,
    private val onTaskClick: (ServiceTask) -> Unit
) : ListAdapter<ServiceTask, ServiceTaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemServiceTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding, onTaskChecked, onTaskClick)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(
        private val binding: ItemServiceTaskBinding,
        private val onTaskChecked: (ServiceTask, Boolean) -> Unit,
        private val onTaskClick: (ServiceTask) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: ServiceTask) {
            binding.apply {
                taskTitleText.text = task.title
                taskDescriptionText.text = task.description
                taskCheckbox.isChecked = task.isCompleted

                taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskChecked(task, isChecked)
                }

                root.setOnClickListener {
                    onTaskClick(task)
                }
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<ServiceTask>() {
        override fun areItemsTheSame(oldItem: ServiceTask, newItem: ServiceTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ServiceTask, newItem: ServiceTask): Boolean {
            return oldItem == newItem
        }
    }
}