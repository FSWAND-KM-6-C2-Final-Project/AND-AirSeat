package com.nafi.airseat.presentation.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nafi.airseat.R
import com.nafi.airseat.data.datasource.flight.FlightApiDataSource
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.data.model.Flight
import com.nafi.airseat.data.repository.FlightRepositoryImpl
import com.nafi.airseat.data.source.network.services.AirSeatApiService
import com.nafi.airseat.databinding.FragmentHomeBinding
import com.nafi.airseat.presentation.calendar.CalendarBottomSheetFragment
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.departcalendar.DepartCalendarFragment
import com.nafi.airseat.presentation.home.adapter.FavoriteDestinationAdapter
import com.nafi.airseat.presentation.passengers.PassengersFragment
import com.nafi.airseat.presentation.resultsearch.ResultSearchActivity
import com.nafi.airseat.presentation.searchticket.SearchTicketFragment
import com.nafi.airseat.presentation.seatclass.SeatClassFragment
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(), CalendarBottomSheetFragment.OnDateSelectedListener, DepartCalendarFragment.OnDateDepartSelectedListener, PassengersFragment.OnPassengerCountUpdatedListener, SeatClassFragment.OnSeatClassSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var selectedStartDate: LocalDate? = null
    private var selectedEndDate: LocalDate? = null
    private var passengerCount: Int? = null
    private var adultCount: Int = 0
    private var childCount: Int = 0
    private var babyCount: Int = 0
    private var selectedDepartAirport: Airport? = null
    private var selectedDestinationAirport: Airport? = null
    private var isReturnFlight: Boolean? = null
    private val favoriteDestinationAdapter: FavoriteDestinationAdapter by lazy {
        FavoriteDestinationAdapter { flight ->
            showConfirmationDialog(flight)
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

        val favoriteDestinationRepository = FlightRepositoryImpl(FlightApiDataSource(AirSeatApiService.invoke()))
        viewModel = HomeViewModel(favoriteDestinationRepository)

        setupUI()
        setupAdapter()
        proceedFavoriteDestination(destinationAirportId = "", departureAirportId = "", orderBy = "", searchDateInput = "", sortByClass = "")
    }

    private fun setupUI() {
        binding.layoutHome.tvDepart.setOnClickListener {
            showBottomSheet(
                SearchTicketFragment {
                    binding.layoutHome.tvDepart.text =
                        getString(
                            R.string.text_airport_result_search_home,
                            it.airportCity,
                            it.airportCityCode,
                        )
                    selectedDepartAirport = it
                    updateSearchButtonState()
                },
            )
        }

        binding.layoutHome.tvDestination.setOnClickListener {
            showBottomSheet(
                SearchTicketFragment {
                    binding.layoutHome.tvDestination.text =
                        getString(
                            R.string.text_airport_result_search_home,
                            it.airportCity,
                            it.airportCityCode,
                        )
                    selectedDestinationAirport = it
                    updateSearchButtonState()
                },
            )
        }

        binding.layoutHome.btnSwap.setOnClickListener {
            val tempAirport = selectedDepartAirport
            selectedDepartAirport = selectedDestinationAirport
            selectedDestinationAirport = tempAirport

            selectedDepartAirport?.let {
                binding.layoutHome.tvDepart.text =
                    getString(
                        R.string.text_airport_result_search_home,
                        it.airportCity,
                        it.airportCityCode,
                    )
            }

            selectedDestinationAirport?.let {
                binding.layoutHome.tvDestination.text =
                    getString(
                        R.string.text_airport_result_search_home,
                        it.airportCity,
                        it.airportCityCode,
                    )
            }
            updateSearchButtonState()
        }

        binding.layoutHome.tvDepartChoose.setOnClickListener {
            if (binding.layoutHome.swDepartReturn.isChecked) {
                val bottomSheet = CalendarBottomSheetFragment(isStartSelection = true)
                bottomSheet.setOnDateSelectedListener(this)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                isReturnFlight = true
            } else {
                val bottomSheet = DepartCalendarFragment(isStartSelection = true)
                bottomSheet.setOnDateDepartSelectedListener(this)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                isReturnFlight = false
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
        }

        binding.layoutHome.btnSearchFlight.setOnClickListener {
            if (passengerCount == null || passengerCount == 0) {
                Snackbar.make(requireView(), "Please select the number of passengers", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDepartAirport == null || selectedDestinationAirport == null) {
                Snackbar.make(requireView(), "Please select the departure and destination airport", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedStartDate == null) {
                Snackbar.make(requireView(), "Please select the departure date", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.layoutHome.tvSeatClassChoose.text.isNullOrEmpty()) {
                Snackbar.make(requireView(), "Please select the seat class", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.layoutHome.swDepartReturn.isChecked && selectedEndDate == null) {
                Snackbar.make(requireView(), "Please select the return date", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedStartDate != null && selectedEndDate != null) {
                val intent = Intent(requireContext(), ResultSearchActivity::class.java)

                selectedStartDate?.let { startDate ->
                    intent.putExtra("startDate", startDate.toString())
                    intent.putExtra("searchDate", startDate.toFormattedString())
                }
                selectedEndDate?.let { endDate ->
                    intent.putExtra("endDate", endDate.toString())
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

                intent.putExtra("passengerCount", passengerCount.toString())
                intent.putExtra("adultCount", adultCount)
                intent.putExtra("childCount", childCount)
                intent.putExtra("babyCount", babyCount)
                intent.putExtra("seatClassChoose", binding.layoutHome.tvSeatClassChoose.text)
                intent.putExtra("isReturnFlight", isReturnFlight)

                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), ResultSearchActivity::class.java)

                selectedStartDate?.let { startDate ->
                    intent.putExtra("startDate", startDate.toString())
                    intent.putExtra("searchDate", startDate.toFormattedString())
                }
                selectedEndDate?.let { endDate ->
                    intent.putExtra("endDate", endDate.toString())
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

                intent.putExtra("passengerCount", passengerCount.toString())
                intent.putExtra("adultCount", adultCount)
                intent.putExtra("childCount", childCount)
                intent.putExtra("babyCount", babyCount)
                intent.putExtra("seatClassChoose", binding.layoutHome.tvSeatClassChoose.text)
                intent.putExtra("isReturnFlight", isReturnFlight)

                startActivity(intent)
            }
        }

        binding.layoutHome.swDepartReturn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutHome.tvReturnTitle.visibility = View.VISIBLE
                binding.layoutHome.tvArrivalChoose.visibility = View.VISIBLE
                binding.layoutHome.tvSpace1.visibility = View.VISIBLE
                binding.layoutHome.tvSpace2.visibility = View.VISIBLE
                isReturnFlight = true
            } else {
                binding.layoutHome.tvSpace1.visibility = View.GONE
                binding.layoutHome.tvSpace2.visibility = View.GONE
                binding.layoutHome.tvReturnTitle.visibility = View.GONE
                binding.layoutHome.tvArrivalChoose.visibility = View.GONE
                isReturnFlight = false
            }
            updateSearchButtonState()
        }
    }

    private fun showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    private fun proceedFavoriteDestination(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departureAirportId: String,
        destinationAirportId: String,
    ) {
        viewModel.getFlightData(
            searchDateInput,
            sortByClass,
            orderBy,
            departureAirportId,
            destinationAirportId,
        ).observe(viewLifecycleOwner) { data ->
            data.proceedWhen(
                doOnSuccess = {
                    binding.csvFavoriteDestination.setState(ContentState.SUCCESS)
                    binding.rvFavorite.isVisible = true
                    data.payload?.let {
                        favoriteDestinationAdapter.submitData(it)
                    }
                },
                doOnLoading = {
                    binding.rvFavorite.isVisible = false
                    binding.csvFavoriteDestination.setState(ContentState.LOADING)
                },
                doOnError = {
                    if (it.exception is NoInternetException) {
                        binding.csvFavoriteDestination.setState(ContentState.ERROR_NETWORK)
                    } else {
                        binding.csvFavoriteDestination.setState(
                            ContentState.ERROR_GENERAL,
                            desc = data.exception?.message.orEmpty(),
                        )
                    }
                    binding.rvFavorite.isVisible = false
                },
                doOnEmpty = {
                    binding.rvFavorite.isVisible = false
                    binding.csvFavoriteDestination.setState(
                        ContentState.EMPTY,
                        desc = "No Favorites Found",
                    )
                },
            )
        }
    }

    private fun setupAdapter() {
        binding.rvFavorite.adapter = favoriteDestinationAdapter
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
        updateSearchButtonState()
    }

    override fun onDateDepartSelected(selectedDepart: LocalDate?) {
        selectedStartDate = selectedDepart

        selectedDepart?.let {
            binding.layoutHome.tvDepartChoose.text = it.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        }
        updateSearchButtonState()
    }

    override fun onPassengerCountUpdated(
        count: Int,
        adultCount: Int,
        childCount: Int,
        babyCount: Int,
    ) {
        passengerCount = count
        this.adultCount = adultCount
        this.childCount = childCount
        this.babyCount = babyCount
        val passengerText = count.toString()
        binding.layoutHome.tvPassengersCount.text = passengerText
        updateSearchButtonState()
    }

    override fun onSeatClassSelected(seatClass: String) {
        binding.layoutHome.tvSeatClassChoose.text = seatClass
        updateSearchButtonState()
    }

    private fun showConfirmationDialog(flight: Flight) {
        val departureCity = flight.departureAirport.airportCity
        val arrivalCity = flight.arrivalAirport.airportCity
        val message = "You have successfully selected the $departureCity - $arrivalCity flight"

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Flight Selected")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            updateHomeFragmentUI(flight)
        }
        builder.show()
    }

    fun LocalDate.toFormattedString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return this.format(formatter)
    }

    private fun updateHomeFragmentUI(flight: Flight) {
        val departureAirport =
            Airport(
                id = flight.departureAirportId,
                airportName = flight.departureAirport.airportName,
                airportCity = flight.departureAirport.airportCity,
                airportCityCode = flight.departureAirport.airportCityCode,
                airportPicture = flight.departureAirport.airportPicture,
            )

        val arrivalAirport =
            Airport(
                id = flight.arrivalAirportId,
                airportName = flight.arrivalAirport.airportName,
                airportCity = flight.arrivalAirport.airportCity,
                airportCityCode = flight.arrivalAirport.airportCityCode,
                airportPicture = flight.arrivalAirport.airportPicture,
            )

        selectedDepartAirport = departureAirport
        selectedDestinationAirport = arrivalAirport
        selectedStartDate = LocalDateTime.parse(flight.departureTime, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
        selectedEndDate = null

        binding.layoutHome.tvDepart.text =
            getString(
                R.string.text_auto_favorite_depart,
                departureAirport.airportCity,
                departureAirport.airportCityCode,
            )
        binding.layoutHome.tvDestination.text =
            getString(
                R.string.text_auto_favorite_destination,
                arrivalAirport.airportCity,
                arrivalAirport.airportCityCode,
            )
        binding.layoutHome.tvDepartChoose.text = selectedStartDate?.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        binding.layoutHome.tvArrivalChoose.text = selectedEndDate?.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        binding.layoutHome.tvSeatClassChoose.text = getString(R.string.text_auto_economy_favorite)
        binding.layoutHome.swDepartReturn.isChecked = false

        updateSearchButtonState()
    }

    private fun validateInputs(): Boolean {
        return selectedDepartAirport != null &&
            selectedDestinationAirport != null &&
            selectedStartDate != null &&
            (binding.layoutHome.swDepartReturn.isChecked && selectedEndDate != null || !binding.layoutHome.swDepartReturn.isChecked)
    }

    private fun updateSearchButtonState() {
        binding.layoutHome.btnSearchFlight.isEnabled = validateInputs()
    }
}
