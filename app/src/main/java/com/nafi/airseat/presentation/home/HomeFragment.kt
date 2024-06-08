package com.nafi.airseat.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.databinding.FragmentHomeBinding
import com.nafi.airseat.presentation.bottomsheetcalendar.BottomSheetCalendarFragment
import com.nafi.airseat.presentation.departcalendar.DepartCalendarFragment
import com.nafi.airseat.presentation.passengers.PassengersFragment
import com.nafi.airseat.presentation.resultsearch.ResultSearchActivity
import com.nafi.airseat.presentation.searchticket.SearchTicketFragment
import com.nafi.airseat.presentation.seatclass.SeatClassFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(), BottomSheetCalendarFragment.OnDateSelectedListener, DepartCalendarFragment.OnDateSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var selectedStartDate: LocalDate? = null
    private var selectedEndDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutHome.tvDepart.setOnClickListener {
            showBottomSheet(SearchTicketFragment())
        }

        binding.layoutHome.tvDestination.setOnClickListener {
            showBottomSheet(SearchTicketFragment())
        }

        binding.layoutHome.tvDepartChoose.setOnClickListener {
            if (binding.layoutHome.swDepartReturn.isChecked) {
                val bottomSheet = BottomSheetCalendarFragment(isStartSelection = true)
                bottomSheet.setOnDateSelectedListener(this)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            } else {
                val bottomSheet = DepartCalendarFragment()
                bottomSheet.setOnDateSelectedListenerDepart(this)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
        }

        binding.layoutHome.tvArrivalChoose.setOnClickListener {
            val bottomSheet = BottomSheetCalendarFragment(isStartSelection = true)
            bottomSheet.setOnDateSelectedListener(this)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.layoutHome.tvPassengersCount.setOnClickListener {
            showBottomSheet(PassengersFragment())
        }

        binding.layoutHome.tvSeatClassChoose.setOnClickListener {
            showBottomSheet(SeatClassFragment())
        }

        binding.layoutHome.btnSearchFlight.setOnClickListener {
            val intent = Intent(requireContext(), ResultSearchActivity::class.java)
            // Pastikan tanggal sudah dipilih
            selectedStartDate?.let { startDate ->
                selectedEndDate?.let { endDate ->
                    intent.putExtra("startDate", startDate.toString())
                    intent.putExtra("endDate", endDate.toString())
                    startActivity(intent)
                }
            }
        }

        binding.layoutHome.swDepartReturn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutHome.tvReturnTitle.visibility = View.VISIBLE
                binding.layoutHome.tvArrivalChoose.visibility = View.VISIBLE
            }
        }
    }

    private fun showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    override fun onDateSelected(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ) {
        // Menyimpan tanggal yang dipilih ke variabel
        selectedStartDate = startDate
        selectedEndDate = endDate

        // Menampilkan tanggal yang dipilih di UI
        if (startDate != null && endDate != null) {
            binding.layoutHome.tvDepartChoose.text = startDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
            binding.layoutHome.tvArrivalChoose.text = endDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        }
    }

    override fun onDateSelectedDepart(startDate: LocalDate?) {
        // Menyimpan tanggal yang dipilih ke variabel
        selectedStartDate = startDate

        // Menampilkan tanggal yang dipilih di UI
        if (startDate != null) {
            binding.layoutHome.tvDepartChoose.text = startDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        }
    }
}
