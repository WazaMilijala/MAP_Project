package com.example.valentinesgarage.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.valentinesgarage.R
import com.example.valentinesgarage.databinding.FragmentReportsBinding
import com.example.valentinesgarage.presentation.viewmodels.ReportsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        viewModel.generateReport()
    }

    private fun observeData() {
        viewModel.employeeProductivity.observe(viewLifecycleOwner) { data: Map<String, Int> ->
            updateProductivityDisplay(data)
        }

        viewModel.vehicleConditionReport.observe(viewLifecycleOwner) { report: List<Pair<String, Int>> ->
            updateConditionDisplay(report)
        }

        viewModel.totalTrucks.observe(viewLifecycleOwner) { total: Int ->
            binding.totalTrucksText.text = total.toString()
        }

        viewModel.completedTasks.observe(viewLifecycleOwner) { completed: Int ->
            binding.completedTasksText.text = completed.toString()
        }
    }

    private fun updateProductivityDisplay(data: Map<String, Int>) {
        val container = binding.productivityContainer
        container.removeAllViews()

        if (data.isEmpty()) {
            addEmptyMessage(container, "No productivity data available")
            return
        }

        data.forEach { (employee: String, count: Int) ->
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 8
                }
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

            row.addView(nameText)
            row.addView(countText)
            container.addView(row)
        }
    }

    private fun updateConditionDisplay(report: List<Pair<String, Int>>) {
        val container = binding.conditionContainer
        container.removeAllViews()

        if (report.isEmpty()) {
            addEmptyMessage(container, "No condition data available")
            return
        }

        report.forEach { (condition: String, count: Int) ->
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 8
                }
            }

            val conditionText = TextView(requireContext()).apply {
                text = condition
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                textSize = 14f
            }

            val countText = TextView(requireContext()).apply {
                text = "$count vehicles"
                textSize = 14f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary_blue))
            }

            row.addView(conditionText)
            row.addView(countText)
            container.addView(row)
        }
    }

    private fun addEmptyMessage(container: LinearLayout, message: String) {
        val emptyText = TextView(requireContext()).apply {
            text = message
            textSize = 14f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_medium))
            gravity = android.view.Gravity.CENTER
        }
        container.addView(emptyText)
    }

    private fun setupListeners() {
        binding.generateReportButton.setOnClickListener {
            viewModel.generateReport()
            Toast.makeText(context, "Report generated", Toast.LENGTH_SHORT).show()
        }

        binding.exportButton.setOnClickListener {
            showExportDialog()
        }

        binding.changeDateRangeButton.setOnClickListener {
            Toast.makeText(context, "Date range selection coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showExportDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Export Report")
            .setMessage("Export report as CSV file?")
            .setPositiveButton("Export") { _, _ ->
                viewModel.exportReport()
                Toast.makeText(context, "Export started", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}