package com.nafi.airseat.presentation.resultsearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResultSearchBinding
import com.nafi.airseat.databinding.HorizontalDayBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.detailflight.DetailFlightActivity
import com.nafi.airseat.presentation.resultsearch.adapter.ResultSearchAdapter
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

class ResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultSearchBinding
    private lateinit var startDate: LocalDate
    private var endDate: LocalDate? = null
    private lateinit var selectedDepart: LocalDate
    private lateinit var selectedDate: LocalDate
    private var typeSeatClass: String? = null
    private val viewModel: ResultSearchViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private val resultAdapter: ResultSearchAdapter by lazy {
        /*ResultSearchAdapter(typeSeatClass = typeSeatClass) {
            navigateToDetailTicket(it.id.toString())
        }*/
        ResultSearchAdapter(typeSeatClass = typeSeatClass) { flight, price ->
            navigateToDetailTicket(flight.id.toString(), price)
        }
    }
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get selected dates from intent
        val startDateString = intent.getStringExtra("startDate")
        val endDateString = intent.getStringExtra("endDate")
        val selectedDepartString = intent.getStringExtra("selectedDepart")
        val searchDateString = intent.getStringExtra("searchDate")
        val searchDateDepartString = intent.getStringExtra("searchDateDepart")
        val departureAirportId = intent.getIntExtra("departAirportId", -1)
        val destinationAirportId = intent.getIntExtra("destinationAirportId", -1)
        val sortByClass = intent.getStringExtra("sortByClass")
        val orderBy = intent.getStringExtra("orderDesc")
        val passengerCount = intent.getStringExtra("passengerCount")
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
        val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
        typeSeatClass = seatClassChoose
        // Check if necessary data is present
        if (startDateString != null && endDateString != null) {
            startDate = LocalDate.parse(startDateString)
            endDate = LocalDate.parse(endDateString)
            selectedDate = startDate // Assuming selectedDate represents either startDate or selectedDepart
        } else if (startDateString != null) {
            startDate = LocalDate.parse(selectedDepartString)
            selectedDate = startDate
        } else {
            // Close the activity if no dates are provided
            finish()
            return
        }

        binding.layoutHeader.btnBackHome.setOnClickListener {
            finish()
        }

        binding.layoutHeader.textName.text = "$airportCityCodeDeparture > $airportCityCodeDestination"
        binding.layoutHeader.textGreetings.text = "$passengerCount Passengers"
        binding.layoutHeader.tvClass.text = seatClassChoose
        Toast.makeText(this, "Selected seat class: $seatClassChoose", Toast.LENGTH_SHORT).show()

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
                            departureAirportId.toString(),
                            destinationAirportId.toString(),
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

        // Setup calendar only with selected dates range
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

        binding.exSevenCalendar.scrollToDate(startDate) // Scroll to start date
        setupAdapter()
        proceedResultTicket(
            searchDateString ?: "",
            sortByClass.toString(),
            orderBy.toString(),
            departureAirportId.toString(),
            destinationAirportId.toString(),
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
                        if (it.isEmpty()) {
                            Toast.makeText(this, "No flights found", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                doOnLoading = {
                    binding.csvResultSearch.setState(ContentState.LOADING)
                    // Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    // Toast.makeText(this, "Error loading flights", Toast.LENGTH_SHORT).show()
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
                    /*clearAdapterData()
                    binding.rvSearchTicket.isVisible = false
                    Toast.makeText(this, "No flights found", Toast.LENGTH_SHORT).show()*/
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

    /*private fun navigateToDetailTicket(id: String) {
        startActivity(
            Intent(this, DetailFlightActivity::class.java).apply {
                putExtra("id", id)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }*/
    private fun navigateToDetailTicket(
        id: String,
        price: Int,
    ) {
        startActivity(
            Intent(this, DetailFlightActivity::class.java).apply {
                putExtra("id", id)
                putExtra("price", price)
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
}
