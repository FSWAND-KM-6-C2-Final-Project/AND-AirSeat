package com.nafi.airseat.presentation.resultsearchreturn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResultSearchReturnBinding
import com.nafi.airseat.databinding.HorizontalDayBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.detailflight.DetailFlightActivity
import com.nafi.airseat.presentation.filter.FilterBottomSheetFragment
import com.nafi.airseat.presentation.resultsearchreturn.adapter.ResultSearchReturnAdapter
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.calendar.displayText
import com.nafi.airseat.utils.calendar.getWeekPageTitle
import com.nafi.airseat.utils.getColorCompat
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ResultSearchReturnActivity : AppCompatActivity(), FilterBottomSheetFragment.OnFilterSelectedListener {
    private lateinit var binding: ActivityResultSearchReturnBinding
    private lateinit var startDate: LocalDate
    private var endDate: LocalDate? = null
    private lateinit var selectedDate: LocalDate
    private var typeSeatClass: String? = null
    private var isReturnFlight: Boolean? = null
    private lateinit var departureAirportId: String
    private lateinit var destinationAirportId: String
    private lateinit var sortByClass: String
    private var orderBy: String? = null
    private val viewModel: ResultSearchReturnViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private val resultAdapter: ResultSearchReturnAdapter by lazy {
        ResultSearchReturnAdapter(typeSeatClass = typeSeatClass) { flight, price ->
            navigateToDetailTicket(flight.id, price)
        }
    }
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchReturnBinding.inflate(layoutInflater)
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
        val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
        val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
        val roundTrip = intent.getBooleanExtra("isReturnFlight", false)
        isReturnFlight = roundTrip
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

        binding.layoutHeaderReturn.btnBackHome.setOnClickListener {
            finish()
        }

        binding.layoutHeaderReturn.textName.text =
            getString(
                R.string.text_code_depart_destination_header,
                airportCityCodeDeparture,
                airportCityCodeDestination,
            )
        binding.layoutHeaderReturn.textGreetings.text =
            getString(R.string.text_count_passenger_header, passengerCount)
        binding.layoutHeaderReturn.tvClass.text = seatClassChoose
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
                        binding.csvResultSearchReturn.setState(ContentState.SUCCESS)
                        resultAdapter.submitData(it)
                        binding.rvSearchTicketReturn.isVisible = it.isNotEmpty()
                    }
                },
                doOnLoading = {
                    binding.rvSearchTicketReturn.isVisible = false
                    binding.csvResultSearchReturn.setState(ContentState.LOADING)
                },
                doOnError = {
                    if (it.exception is NoInternetException) {
                        binding.csvResultSearchReturn.setState(ContentState.ERROR_NETWORK)
                    } else {
                        binding.csvResultSearchReturn.setState(
                            ContentState.ERROR_GENERAL,
                            desc = result.exception?.message.orEmpty(),
                        )
                    }
                },
                doOnEmpty = {
                    clearAdapterData()
                    binding.rvSearchTicketReturn.isVisible = false
                    binding.csvResultSearchReturn.setState(
                        ContentState.EMPTY,
                        desc = "No Flights Found",
                    )
                },
            )
        }
    }

    private fun setupAdapter() {
        binding.rvSearchTicketReturn.adapter = resultAdapter
    }

    private fun navigateToDetailTicket(
        id: Int,
        price: Int,
    ) {
        startActivity(
            Intent(this, DetailFlightActivity::class.java).apply {
                putExtra("returnFlight", id)
                putExtra("priceReturn", price)
                putExtra("isReturnFlight", isReturnFlight)
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
        binding.mcvFilterReturn.setOnClickListener {
            val bottomSheet = FilterBottomSheetFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    override fun onFilterSelected(filterSelected: String) {
        binding.llResultFilterReturn.isVisible = true
        binding.tvResultFilterReturn.text = filterSelected
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
