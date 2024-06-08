package com.nafi.airseat.presentation.resultsearch

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResultSearchBinding
import com.nafi.airseat.databinding.HorizontalDayBinding
import com.nafi.airseat.utils.calendar.displayText
import com.nafi.airseat.utils.calendar.getWeekPageTitle
import com.nafi.airseat.utils.getColorCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultSearchBinding
    private lateinit var startDate: LocalDate
    private lateinit var endDate: LocalDate
    private lateinit var selectedDate: LocalDate
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get selected dates from intent
        val startDateString = intent.getStringExtra("startDate")
        val endDateString = intent.getStringExtra("endDate")

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
                        oldDate?.let { binding.exSevenCalendar.notifyDateChanged(it) }
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
    }
}
