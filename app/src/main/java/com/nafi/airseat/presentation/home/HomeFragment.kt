package com.nafi.airseat.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.data.datasource.favoritedestination.FavoriteDestinationDataSourceImpl
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.data.model.FavoriteDestination
import com.nafi.airseat.data.repository.FavoriteDestinationRepositoryImpl
import com.nafi.airseat.databinding.FragmentHomeBinding
import com.nafi.airseat.presentation.blank.BlankActivity
import com.nafi.airseat.presentation.calendar.CalendarBottomSheetFragment
import com.nafi.airseat.presentation.common.sharedviewmodel.SharedViewModel
import com.nafi.airseat.presentation.departcalendar.DepartCalendarFragment
import com.nafi.airseat.presentation.home.adapter.FavoriteDestinationAdapter
import com.nafi.airseat.presentation.passengers.PassengersFragment
import com.nafi.airseat.presentation.resultsearch.ResultSearchActivity
import com.nafi.airseat.presentation.searchticket.SearchTicketFragment
import com.nafi.airseat.presentation.seatclass.SeatClassFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(), CalendarBottomSheetFragment.OnDateSelectedListener, DepartCalendarFragment.OnDateSelectedListener, PassengersFragment.OnPassengerCountUpdatedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var selectedStartDate: LocalDate? = null
    private var selectedEndDate: LocalDate? = null
    private lateinit var sharedViewModel: SharedViewModel
    private val favoriteDestinationAdapter: FavoriteDestinationAdapter by lazy {
        FavoriteDestinationAdapter {
            BlankActivity.startActivity(requireContext(), it.id.toString())
        }
    }
    var isDepartSelected = false

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
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val favoriteDestinationRepository = FavoriteDestinationRepositoryImpl(FavoriteDestinationDataSourceImpl())
        viewModel = HomeViewModel(favoriteDestinationRepository)

        setupUI()
        setupFavoriteDestination()
        proceedFavoriteDestination()

        sharedViewModel.departAirport.observe(viewLifecycleOwner) { airport ->
            binding.layoutHome.tvDepart.text = airport.airportName
        }

        sharedViewModel.destinationAirport.observe(viewLifecycleOwner) { airport ->
            binding.layoutHome.tvDestination.text = airport.airportName
        }
    }

    private fun setupUI() {
        binding.layoutHome.tvDepart.setOnClickListener {
            showBottomSheet(SearchTicketFragment())
        }

        binding.layoutHome.tvDestination.setOnClickListener {
            showBottomSheet(SearchTicketFragment())
        }

        binding.layoutHome.tvDepartChoose.setOnClickListener {
            if (binding.layoutHome.swDepartReturn.isChecked) {
                val bottomSheet = CalendarBottomSheetFragment(isStartSelection = true)
                bottomSheet.setOnDateSelectedListener(this)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            } else {
                val bottomSheet = DepartCalendarFragment()
                bottomSheet.setOnDateSelectedListenerDepart(this)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
        }

        binding.layoutHome.tvArrivalChoose.setOnClickListener {
            val bottomSheet = CalendarBottomSheetFragment(isStartSelection = true)
            bottomSheet.setOnDateSelectedListener(this)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.layoutHome.tvPassengersCount.setOnClickListener {
            val bottomSheet = PassengersFragment()
            bottomSheet.setOnPassengerCountUpdatedListener(this)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.layoutHome.tvSeatClassChoose.setOnClickListener {
            showBottomSheet(SeatClassFragment())
        }

        binding.layoutHome.btnSearchFlight.setOnClickListener {
            val intent = Intent(requireContext(), ResultSearchActivity::class.java)
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

    fun onAirportSelected(airport: Airport) {
        binding.layoutHome.tvDepart.text = airport.airportName
    }

    private fun setupFavoriteDestination() {
        binding.rvFavorite.apply {
            adapter = favoriteDestinationAdapter
        }
    }

    private fun proceedFavoriteDestination() {
        viewModel.getFavoriteDestinations().observe(viewLifecycleOwner) { data ->
            data?.let {
                bindFavoriteDestinationList(it)
            }
        }
    }

    private fun bindFavoriteDestinationList(data: List<FavoriteDestination>) {
        favoriteDestinationAdapter.submitData(data)
    }

    override fun onDateSelected(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ) {
        selectedStartDate = startDate
        selectedEndDate = endDate

        if (startDate != null && endDate != null) {
            binding.layoutHome.tvDepartChoose.text = startDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
            binding.layoutHome.tvArrivalChoose.text = endDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        }
    }

    override fun onDateSelectedDepart(startDate: LocalDate?) {
        selectedStartDate = startDate

        if (startDate != null) {
            binding.layoutHome.tvDepartChoose.text = startDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        }
    }

    override fun onPassengerCountUpdated(count: Int) {
        binding.layoutHome.tvPassengersCount.text = count.toString()
    }
}
