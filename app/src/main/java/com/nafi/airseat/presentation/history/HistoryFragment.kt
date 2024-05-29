package com.nafi.airseat.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.nafi.airseat.databinding.FragmentHistoryBinding
import com.nafi.airseat.presentation.calendar.CalendarBottomSheetFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModels()

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivSearchHistory.setOnClickListener {
            // showDateRangePicker()
            val dialog = CalendarBottomSheetFragment()
            dialog.show(childFragmentManager, dialog.tag)
        }
    }

    private fun showDateRangePicker() {
        // Create constraints to allow only future dates
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        // Initialize the date range picker
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(
                    androidx.core.util.Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds(),
                    ),
                )
                .build()

        // Show the date range picker
        dateRangePicker.show(parentFragmentManager, "date_range_picker")

        // Handle the selection result
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            // selection is a Pair<Long, Long> with the start and end dates
            val startDate = selection.first
            val endDate = selection.second

            // Format the dates to a readable format
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val startDateString = dateFormat.format(Date(startDate))
            val endDateString = dateFormat.format(Date(endDate))

            // Show the selected dates in a Toast message
            Toast.makeText(requireContext(), "Selected range: $startDateString - $endDateString", Toast.LENGTH_LONG).show()
        }
    }
}
