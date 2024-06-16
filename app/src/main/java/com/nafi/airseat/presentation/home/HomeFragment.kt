package com.nafi.airseat.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class HomeFragment : Fragment(), CalendarBottomSheetFragment.OnDateSelectedListener, DepartCalendarFragment.OnDateDepartSelectedListener, PassengersFragment.OnPassengerCountUpdatedListener, SeatClassFragment.OnSeatClassSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var selectedStartDate: LocalDate? = null
    private var selectedEndDate: LocalDate? = null
    private var selectedDeparts: LocalDate? = null
    private var selectedSeatClass: String? = null
    private lateinit var sharedViewModel: SharedViewModel
    private var selectedDepartAirport: Airport? = null
    private var selectedDestinationAirport: Airport? = null
    private val favoriteDestinationAdapter: FavoriteDestinationAdapter by lazy {
        FavoriteDestinationAdapter {
            BlankActivity.startActivity(requireContext(), it.id.toString())
        }
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
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val favoriteDestinationRepository = FavoriteDestinationRepositoryImpl(FavoriteDestinationDataSourceImpl())
        viewModel = HomeViewModel(favoriteDestinationRepository)

        setupUI()
        setupFavoriteDestination()
        proceedFavoriteDestination()
    }

    private fun setupUI() {
        binding.layoutHome.tvDepart.setOnClickListener {
            showBottomSheet(
                SearchTicketFragment {
                    binding.layoutHome.tvDepart.text = "${it.airportCity} (${it.airportCityCode})"
                    selectedDepartAirport = it
                },
            )
        }

        binding.layoutHome.tvDestination.setOnClickListener {
            showBottomSheet(
                SearchTicketFragment {
                    binding.layoutHome.tvDestination.text = "${it.airportCity} (${it.airportCityCode})"
                    selectedDestinationAirport = it
                },
            )
        }

        binding.layoutHome.btnSwap.setOnClickListener {
            val tempAirport = selectedDepartAirport
            selectedDepartAirport = selectedDestinationAirport
            selectedDestinationAirport = tempAirport

            selectedDepartAirport?.let {
                binding.layoutHome.tvDepart.text = "${it.airportCity} (${it.airportCityCode})"
            }

            selectedDestinationAirport?.let {
                binding.layoutHome.tvDestination.text = "${it.airportCity} (${it.airportCityCode})"
            }
        }

        binding.layoutHome.tvDepartChoose.setOnClickListener {
            if (binding.layoutHome.swDepartReturn.isChecked) {
                val bottomSheet = CalendarBottomSheetFragment(isStartSelection = true)
                bottomSheet.setOnDateSelectedListener(this)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            } else {
                val bottomSheet = DepartCalendarFragment(isStartSelection = true)
                bottomSheet.setOnDateDepartSelectedListener(this)
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
            val seatClassFragment = SeatClassFragment()
            seatClassFragment.setOnSeatClassSelectedListener(this)
            showBottomSheet(seatClassFragment)
            // showBottomSheet(SeatClassFragment())
        }

        binding.layoutHome.btnSearchFlight.setOnClickListener {
            if (selectedStartDate != null) {
                val intent = Intent(requireContext(), ResultSearchActivity::class.java)
                selectedStartDate?.let { startDate ->
                    selectedEndDate?.let { endDate ->
                        intent.putExtra("startDate", startDate.toString())
                        intent.putExtra("endDate", endDate.toString())
                        intent.putExtra("searchDate", startDate.toFormattedString())
                    }
                }
                selectedStartDate?.let { selectedDepart ->
                    intent.putExtra("selectedDepart", selectedDepart.toString())
                    intent.putExtra("searchDateDepart", selectedDepart.toFormattedString())
                }
                selectedDepartAirport?.let { departAirport ->
                    intent.putExtra("departAirportId", departAirport.id)
                    intent.putExtra("airportCityCodeDeparture", departAirport.airportCityCode)
                }
                selectedDestinationAirport?.let { destinationAirport ->
                    intent.putExtra("destinationAirportId", destinationAirport.id)
                    intent.putExtra("airportCityCodeDestination", destinationAirport.airportCityCode)
                }

                intent.putExtra("passengerCount", binding.layoutHome.tvPassengersCount.text.toString())

                Log.d(
                    "HomeFragment",
                    "Depart Airport ID: ${selectedDepartAirport?.id}, Destination Airport ID: ${selectedDestinationAirport?.id}",
                )
                startActivity(intent)
            } else {
                // Handle case where selectedStartDate is null
                Toast.makeText(requireContext(), "Please select a start date", Toast.LENGTH_SHORT).show()
            }
        }

        binding.layoutHome.swDepartReturn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutHome.tvReturnTitle.visibility = View.VISIBLE
                binding.layoutHome.tvArrivalChoose.visibility = View.VISIBLE
            } else {
                binding.layoutHome.tvReturnTitle.visibility = View.INVISIBLE
                binding.layoutHome.tvArrivalChoose.visibility = View.INVISIBLE
            }
        }
    }

    private fun showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
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

    override fun onDateDepartSelected(selectedDepart: LocalDate?) {
        selectedStartDate = selectedDepart

        selectedDepart?.let {
            binding.layoutHome.tvDepartChoose.text = it.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        }
    }

    override fun onPassengerCountUpdated(count: Int) {
        binding.layoutHome.tvPassengersCount.text = count.toString()
    }

    override fun onSeatClassSelected(seatClass: String) {
        binding.layoutHome.tvSeatClassChoose.text = seatClass
    }

    fun LocalDate.toFormattedString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return this.format(formatter)
    }
}
