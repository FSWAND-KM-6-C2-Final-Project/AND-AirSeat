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
import com.nafi.airseat.presentation.detailflight.DetailFlightActivity
import com.nafi.airseat.presentation.resultsearch.adapter.ResultSearchAdapter
import com.nafi.airseat.utils.calendar.displayText
import com.nafi.airseat.utils.calendar.getWeekPageTitle
import com.nafi.airseat.utils.getColorCompat
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultSearchBinding
    private lateinit var startDate: LocalDate
    private lateinit var endDate: LocalDate
    private lateinit var selectedDate: LocalDate
    private val viewModel: ResultSearchViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private val resultAdapter: ResultSearchAdapter by lazy {
        ResultSearchAdapter {
            // DetailFlightActivity.startActivity(this, it.id.toString())
            navigateToDetailTicket(it.id.toString())
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
        val departureAirportId = intent.getIntExtra("departAirportId", -1)
        val destinationAirportId = intent.getIntExtra("destinationAirportId", -1)

        if (startDateString != null && endDateString != null) {
            startDate = LocalDate.parse(startDateString)
            endDate = LocalDate.parse(endDateString)
            selectedDate = startDate
        } else {
            finish() // Close the activity if no dates are provided
            return
        }

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
        binding.exSevenCalendar.setup(
            startDate,
            endDate,
            firstDayOfWeekFromLocale(),
        )
        binding.exSevenCalendar.scrollToDate(startDate) // Scroll to start date
        setupAdapter()
        proceedResultTicket(departureAirportId.toString(), destinationAirportId.toString())
    }

    private fun proceedResultTicket(
        departureAirportId: String,
        destinationAirportId: String,
    ) {
        viewModel.getFlightData(departureAirportId, destinationAirportId).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    result.payload?.let {
                        resultAdapter.submitData(it)
                        // Toast.makeText(this, "${it.size}", Toast.LENGTH_SHORT).show()
                    }
                },
                doOnLoading = {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                },
                doOnEmpty = {
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun setupAdapter() {
        binding.rvSearchTicket.adapter = this@ResultSearchActivity.resultAdapter
    }

    private fun navigateToDetailTicket(id: String) {
        startActivity(
            Intent(this, DetailFlightActivity::class.java).apply {
                putExtra("id", id)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
