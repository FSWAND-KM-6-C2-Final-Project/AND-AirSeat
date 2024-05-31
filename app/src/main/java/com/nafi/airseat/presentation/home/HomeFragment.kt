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
import com.nafi.airseat.presentation.calendar.CalendarBottomSheetFragment
import com.nafi.airseat.presentation.passengers.PassengersFragment
import com.nafi.airseat.presentation.resultsearch.ResultSearchActivity
import com.nafi.airseat.presentation.searchticket.SearchTicketFragment
import com.nafi.airseat.presentation.seatclass.SeatClassFragment

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

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
            Log.d("HomeFragment", "Calendar button clicked")
            showBottomSheet(CalendarBottomSheetFragment())
        }

        binding.layoutHome.tvArrivalChoose.setOnClickListener {
            Log.d("HomeFragment", "Calendar button clicked")
            showBottomSheet(CalendarBottomSheetFragment())
        }

        binding.layoutHome.tvPassengersCount.setOnClickListener {
            showBottomSheet(PassengersFragment())
        }

        binding.layoutHome.tvSeatClassChoose.setOnClickListener {
            showBottomSheet(SeatClassFragment())
        }

        binding.layoutHome.btnSearchFlight.setOnClickListener {
            startActivity(Intent(requireContext(), ResultSearchActivity::class.java))
        }
    }

    private fun showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
}
