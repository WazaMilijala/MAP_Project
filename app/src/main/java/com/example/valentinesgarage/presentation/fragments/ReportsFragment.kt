package com.example.valentinesgarage.presentation.fragments


import android.graphics.Typeface

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.valentinesgarage.R
import com.example.valentinesgarage.databinding.FragmentReportsBinding
import com.example.valentinesgarage.presentation.viewmodels.ReportsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeData()
    }

    private fun observeData() {
        // Summary statistics
        viewModel.totalTrucks.observe(viewLifecycleOwner) { total: Int ->
            binding.totalTrucksText.text = total.toString()
        }

        viewModel.completedTasks.observe(viewLifecycleOwner) { completed: Int ->
            binding.completedTasksText.text = completed.toString()
        }

        viewModel.pendingTasks.observe(viewLifecycleOwner) { pending: Int ->
            binding.pendingTasksText.text = pending.toString()
        }

        viewModel.averageServiceTime.observe(viewLifecycleOwner) { avgTime: String ->
            binding.avgServiceTimeText.text = avgTime
        }

        // Truck status distribution
        viewModel.truckStatusReport.observe(viewLifecycleOwner) { report: List<Pair<String, Int>> ->
            updateTruckStatusDisplay(report)
        }

        // Vehicle condition report
        viewModel.vehicleConditionReport.observe(viewLifecycleOwner) { report: List<Pair<String, Int>> ->
            updateConditionDisplay(report)
        }

        // Employee productivity
        viewModel.employeeProductivity.observe(viewLifecycleOwner) { data: Map<String, Int> ->
            updateProductivityDisplay(data)
        }

        // Recent activity
        viewModel.recentTrucks.observe(viewLifecycleOwner) { trucks: List<String> ->
            updateRecentActivityDisplay(trucks)
        }

        // Date range
        viewModel.reportDateRange.observe(viewLifecycleOwner) { dateRange: String ->
            binding.reportDateRangeText.text = dateRange
        }

        // Loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.generateReportButton.isEnabled = !isLoading
            if (isLoading) {
                binding.generateReportButton.text = "Generating..."
            } else {
                binding.generateReportButton.text = "Generate Full Report"
            }
        }
    }

    private fun setupListeners() {
        binding.generateReportButton.setOnClickListener {
            viewModel.generateReport()
            Toast.makeText(context, "Generating report...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTruckStatusDisplay(report: List<Pair<String, Int>>) {
        val container = binding.truckStatusContainer
        container.removeAllViews()

        if (report.isEmpty() || report.all { it.first == "No data" }) {
            addEmptyMessage(container, "No status data available. Click 'Generate Report' to load data.")
            return
        }

        val maxCount = report.maxOf { it.second }.toFloat()

        report.forEach { (status, count) ->
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 12
                }
            }

            // Status label and count
            val headerRow = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val statusText = TextView(requireContext()).apply {
                text = status.replace("_", " ").lowercase()
                    .replaceFirstChar { it.uppercase() }
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                textSize = 14f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }

            val countText = TextView(requireContext()).apply {
                text = count.toString()
                textSize = 14f
                //textStyle = android.graphics.Typeface.BOLD

                setTypeface(null, Typeface.BOLD)

                setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_red))
            }

            headerRow.addView(statusText)
            headerRow.addView(countText)

            // Progress bar
            val progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    16
                ).apply {
                    topMargin = 4
                }
                max = 100
                progress = if (maxCount > 0) ((count / maxCount) * 100).toInt() else 0
                progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_red)
                progressBackgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray_light)
            }

            row.addView(headerRow)
            row.addView(progressBar)
            container.addView(row)
        }
    }

    private fun updateConditionDisplay(report: List<Pair<String, Int>>) {
        val container = binding.conditionContainer
        container.removeAllViews()

        if (report.isEmpty() || report.all { it.first == "No data" }) {
            addEmptyMessage(container, "No condition data available. Click 'Generate Report' to load data.")
            return
        }

        val totalCount = report.sumOf { it.second }

        report.forEach { (condition, count) ->
            val percentage = if (totalCount > 0) (count.toFloat() / totalCount * 100).toInt() else 0

            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 12
                }
            }

            val conditionText = TextView(requireContext()).apply {
                text = condition
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.4f
                )
                textSize = 14f
            }

            // Progress bar wrapper
            val progressWrapper = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.4f
                )
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    12,
                    1f
                )
                max = 100
                progress = percentage
                progressTintList = ContextCompat.getColorStateList(requireContext(), when (condition) {
                    "Excellent" -> R.color.success_green
                    "Good" -> R.color.secondary_blue
                    "Fair" -> R.color.warning_orange
                    "Poor" -> R.color.error_red
                    else -> R.color.gray_medium
                })
            }

            val countText = TextView(requireContext()).apply {
                text = "$count ($percentage%)"
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.2f
                )
                textSize = 12f
                gravity = android.view.Gravity.END
            }

            progressWrapper.addView(progressBar)
            row.addView(conditionText)
            row.addView(progressWrapper)
            row.addView(countText)
            container.addView(row)
        }
    }

    private fun updateProductivityDisplay(data: Map<String, Int>) {
        val container = binding.productivityContainer
        container.removeAllViews()

        if (data.isEmpty() || data.all { it.key == "No data" }) {
            addEmptyMessage(container, "No productivity data available. Click 'Generate Report' to load data.")
            return
        }

        val maxTasks = data.values.maxOrNull() ?: 1

        data.forEach { (employee, count) ->
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 12
                }
            }

            val headerRow = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val nameText = TextView(requireContext()).apply {
                text = employee
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                textSize = 14f
            }

            val countText = TextView(requireContext()).apply {
                text = "$count tasks"
                textSize = 14f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_red))
            }

            headerRow.addView(nameText)
            headerRow.addView(countText)

            val progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    12
                ).apply {
                    topMargin = 4
                }
                max = maxTasks
                progress = count
                progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.success_green)
                progressBackgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray_light)
            }

            row.addView(headerRow)
            row.addView(progressBar)
            container.addView(row)
        }
    }

    private fun updateRecentActivityDisplay(trucks: List<String>) {
        val container = binding.recentActivityContainer
        container.removeAllViews()

        if (trucks.isEmpty()) {
            addEmptyMessage(container, "No recent activity")
            return
        }

        trucks.forEachIndexed { index, truck ->
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = if (index < trucks.size - 1) 8 else 0
                }
            }

            val dotView = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(8, 8).apply {
                    gravity = android.view.Gravity.CENTER_VERTICAL
                    marginEnd = 12
                }
                setBackgroundResource(R.drawable.circle_dot)
            }

            val truckText = TextView(requireContext()).apply {
                text = truck
                textSize = 14f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_dark))
            }

            row.addView(dotView)
            row.addView(truckText)
            container.addView(row)
        }
    }

    private fun addEmptyMessage(container: LinearLayout, message: String) {
        val emptyText = TextView(requireContext()).apply {
            text = message
            textSize = 14f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_medium))
            gravity = android.view.Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 8
                bottomMargin = 8
            }
        }
        container.addView(emptyText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}