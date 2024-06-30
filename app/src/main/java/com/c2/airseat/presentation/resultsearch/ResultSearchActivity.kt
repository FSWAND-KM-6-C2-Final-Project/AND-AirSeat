package com.c2.airseat.presentation.resultsearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.c2.airseat.R
import com.c2.airseat.databinding.ActivityResultSearchBinding
import com.c2.airseat.databinding.HorizontalDayBinding
import com.c2.airseat.presentation.common.views.ContentState
import com.c2.airseat.presentation.detailflight.DetailFlightActivity
import com.c2.airseat.presentation.filter.FilterBottomSheetFragment
import com.c2.airseat.presentation.resultsearch.adapter.ResultSearchAdapter
import com.c2.airseat.utils.NoInternetException
import com.c2.airseat.utils.calendar.displayText
import com.c2.airseat.utils.calendar.getColorCompat
import com.c2.airseat.utils.calendar.getWeekPageTitle
import com.c2.airseat.utils.proceedWhen
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ResultSearchActivity : AppCompatActivity(), FilterBottomSheetFragment.OnFilterSelectedListener {
    private lateinit var binding: ActivityResultSearchBinding
    private lateinit var startDate: LocalDate
    private var endDate: LocalDate? = null
    private lateinit var selectedDate: LocalDate
    private var typeSeatClass: String? = null
    private var isReturnFlight: Boolean? = null
    private lateinit var departureAirportId: String
    private lateinit var destinationAirportId: String
    private lateinit var sortByClass: String
    private var orderBy: String? = null
    private lateinit var airportCityCodeDeparture: String
    private lateinit var airportCityCodeDestination: String
    private val viewModel: ResultSearchViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private val resultAdapter: ResultSearchAdapter by lazy {
        ResultSearchAdapter(typeSeatClass = typeSeatClass) { flight, price ->
            navigateToDetailTicket(flight.id.toString(), price, airportCityCodeDeparture, airportCityCodeDestination)
        }
    }
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startDateString = intent.getStringExtra("startDate")
        val endDateString = intent.getStringExtra("endDate")
        val selectedDepartString = intent.getStringExtra("selectedDepart")
        val searchDateString = intent.getStringExtra("searchDate")
        departureAirportId = intent.getIntExtra("departAirportId", -1).toString()
        destinationAirportId = intent.getIntExtra("destinationAirportId", -1).toString()
        val sortByClass = intent.getStringExtra("sortByClass")
        val orderBy = intent.getStringExtra("orderDesc")
        val passengerCount = intent.getStringExtra("passengerCount")
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        val roundTrip = intent.getBooleanExtra("isReturnFlight", false)
        isReturnFlight = roundTrip
        airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture").orEmpty()
        airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination").orEmpty()
        typeSeatClass = seatClassChoose
        if (startDateString != null && endDateString != null) {
            startDate = LocalDate.parse(startDateString)
            endDate = LocalDate.parse(endDateString)
            selectedDate = startDate
        } else if (startDateString != null) {
            startDate = LocalDate.parse(selectedDepartString)
            selectedDate = startDate
        } else {
            finish()
            return
        }

        binding.layoutHeader.btnBackHome.setOnClickListener {
            finish()
        }

        binding.layoutHeader.textName.text =
            getString(
                R.string.text_code_depart_destination_header,
                airportCityCodeDeparture,
                airportCityCodeDestination,
            )
        binding.layoutHeader.textGreetings.text = getString(R.string.text_count_passenger_header, passengerCount)
        binding.layoutHeader.tvClass.text = seatClassChoose
        setupFilter()

        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = HorizontalDayBinding.bind(view)
            lateinit var day: WeekDay

            init {
                view.setOnClickListener {
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.exSevenCalendar.notifyDateChanged(day.date)
                        oldDate.let { binding.exSevenCalendar.notifyDateChanged(it) }
                        proceedResultTicket(
                            selectedDate.toFormattedString(),
                            sortByClass.toString(),
                            orderBy.toString(),
                            departureAirportId,
                            destinationAirportId,
                        )
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                bind.exSevenDateText.text = dateFormatter.format(day.date)
                bind.exSevenDayText.text = day.date.dayOfWeek.displayText()

                val colorRes =
                    when {
                        day.date == selectedDate -> R.color.md_theme_primaryContainer
                        day.date.month == startDate.month -> R.color.md_theme_onPrimary
                        else -> R.color.md_theme_onPrimary
                    }
                bind.exSevenDateText.setTextColor(view.context.getColorCompat(colorRes))
                bind.exSevenSelectedView.isVisible = day.date == selectedDate
            }
        }

        binding.exSevenCalendar.dayBinder =
            object : WeekDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(
                    container: DayViewContainer,
                    data: WeekDay,
                ) = container.bind(data)
            }

        binding.exSevenCalendar.weekScrollListener = { weekDays ->
            binding.exSevenToolbar.title = getWeekPageTitle(weekDays)
        }

        if (endDate == null) {
            val currentMonth = YearMonth.now()
            binding.exSevenCalendar.setup(
                startDate,
                currentMonth.plusMonths(5).atEndOfMonth(),
                firstDayOfWeekFromLocale(),
            )
        } else {
            binding.exSevenCalendar.setup(
                startDate,
                endDate!!,
                firstDayOfWeekFromLocale(),
            )
        }

        binding.exSevenCalendar.scrollToDate(startDate)
        setupAdapter()
        proceedResultTicket(
            searchDateString ?: "",
            sortByClass.toString(),
            orderBy.toString(),
            departureAirportId,
            destinationAirportId,
        )
    }

    private fun proceedResultTicket(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departureAirportId: String,
        destinationAirportId: String,
    ) {
        viewModel.getFlightData(searchDateInput, sortByClass, orderBy, departureAirportId, destinationAirportId).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    result.payload?.let {
                        binding.csvResultSearch.setState(ContentState.SUCCESS)
                        resultAdapter.submitData(it)
                        binding.rvSearchTicket.isVisible = it.isNotEmpty()
                    }
                },
                doOnLoading = {
                    binding.rvSearchTicket.isVisible = false
                    binding.csvResultSearch.setState(ContentState.LOADING)
                },
                doOnError = {
                    if (it.exception is NoInternetException) {
                        binding.csvResultSearch.setState(ContentState.ERROR_NETWORK)
                    } else {
                        binding.csvResultSearch.setState(
                            ContentState.ERROR_GENERAL,
                            desc = result.exception?.message.orEmpty(),
                        )
                    }
                },
                doOnEmpty = {
                    clearAdapterData()
                    binding.rvSearchTicket.isVisible = false
                    binding.csvResultSearch.setState(
                        ContentState.EMPTY,
                        desc = "No Flights Found",
                    )
                },
            )
        }
    }

    private fun setupAdapter() {
        binding.rvSearchTicket.adapter = resultAdapter
    }

    private fun navigateToDetailTicket(
        id: String,
        price: Int,
        airportCityCodeDeparture: String,
        airportCityCodeDestination: String,
    ) {
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)

        startActivity(
            Intent(this, DetailFlightActivity::class.java).apply {
                putExtra("id", id)
                putExtra("priceDepart", price)
                putExtra("departAirportId", intent.getIntExtra("departAirportId", -1))
                putExtra("destinationAirportId", intent.getIntExtra("destinationAirportId", -1))
                putExtra("endDate", intent.getStringExtra("endDate"))
                putExtra("isReturnFlight", isReturnFlight)
                putExtra("seatClassChoose", typeSeatClass)
                putExtra("passengerCount", intent.getStringExtra("passengerCount"))
                putExtra("price", price)
                putExtra("airportCityCodeDeparture", airportCityCodeDeparture)
                putExtra("airportCityCodeDestination", airportCityCodeDestination)
                putExtra("seatClassChoose", seatClassChoose)
                putExtra("adultCount", adultCount)
                putExtra("childCount", childCount)
                putExtra("babyCount", babyCount)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun clearAdapterData() {
        resultAdapter.submitData(emptyList())
    }

    private fun LocalDate.toFormattedString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return this.format(formatter)
    }

    private fun setupFilter() {
        binding.mcvFilter.setOnClickListener {
            val bottomSheet = FilterBottomSheetFragment()
            bottomSheet.setOnFilterSelectedListener(this)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    override fun onFilterSelected(filterSelected: String) {
        binding.llResultFilter.isVisible = true
        binding.tvResultFilter.text = filterSelected
        when (filterSelected) {
            "Cheapest Price" -> {
                when (typeSeatClass) {
                    "Economy" -> sortByClass = "price_economy"
                    "Premium Economy" -> sortByClass = "price_premium_economy"
                    "Business" -> sortByClass = "price_business"
                    "First Class" -> sortByClass = "price_first_class"
                    else ->
                        sortByClass =
                            null.toString()
                }
                orderBy = "asc"
            }
            "Highest Price" -> {
                when (typeSeatClass) {
                    "Economy" -> sortByClass = "price_economy"
                    "Premium Economy" -> sortByClass = "price_premium_economy"
                    "Business" -> sortByClass = "price_business"
                    "First Class" -> sortByClass = "price_first_class"
                    else ->
                        sortByClass =
                            null.toString()
                }
                orderBy = "desc"
            }
            "Earliest - Departure" -> {
                sortByClass = "departureTime"
                orderBy = "asc"
            }
            "Very End - Departure" -> {
                sortByClass = "departureTime"
                orderBy = "desc"
            }
        }

        proceedResultTicket(
            selectedDate.toFormattedString(),
            sortByClass,
            orderBy.toString(),
            departureAirportId,
            destinationAirportId,
        )
    }
}
