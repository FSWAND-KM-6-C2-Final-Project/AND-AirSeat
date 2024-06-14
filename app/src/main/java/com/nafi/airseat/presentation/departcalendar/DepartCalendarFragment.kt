package com.nafi.airseat.presentation.departcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.nafi.airseat.R
import com.nafi.airseat.databinding.Example2CalendarDayBinding
import com.nafi.airseat.databinding.Example2CalendarHeaderBinding
import com.nafi.airseat.databinding.FragmentDepartCalendarBinding
import com.nafi.airseat.utils.calendar.displayText
import com.nafi.airseat.utils.makeInVisible
import com.nafi.airseat.utils.makeVisible
import com.nafi.airseat.utils.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class DepartCalendarFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDepartCalendarBinding

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private var onDateSelectedListener: OnDateSelectedListener? = null

    interface OnDateSelectedListener {
        fun onDateSelectedDepart(startDate: LocalDate?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDepartCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val daysOfWeek = daysOfWeek()
        binding.legendLayout.root.children.forEachIndexed { index, child ->
            (child as TextView).apply {
                text = daysOfWeek[index].name.first().toString()
                setTextColorRes(R.color.md_theme_onPrimary)
            }
        }

        configureBinders()
        binding.exTwoCalendar.setup(
            YearMonth.now(),
            YearMonth.now().plusMonths(200),
            daysOfWeek.first(),
        )

        binding.btnSave.setOnClickListener {
            val date = selectedDate
            if (date != null) {
                onDateSelectedListener?.onDateSelectedDepart(date) // Panggil metode ini saat tanggal dipilih
                val text = "Selected: ${DateTimeFormatter.ofPattern("d MMMM yyyy").format(date)}"
                Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
                dismiss()
            } else {
                Snackbar.make(requireView(), "No date selected", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureBinders() {
        val calendarView = binding.exTwoCalendar

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = Example2CalendarDayBinding.bind(view).exTwoDayText

            init {
                textView.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            calendarView.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            calendarView.notifyDateChanged(day.date)
                            oldDate?.let { calendarView.notifyDateChanged(oldDate) }
                        }
                    }
                }
            }
        }

        calendarView.dayBinder =
            object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(
                    container: DayViewContainer,
                    data: CalendarDay,
                ) {
                    container.day = data
                    val textView = container.textView
                    textView.text = data.date.dayOfMonth.toString()

                    if (data.position == DayPosition.MonthDate) {
                        textView.makeVisible()
                        when (data.date) {
                            selectedDate -> {
                                textView.setTextColorRes(R.color.md_theme_onPrimary)
                                textView.setBackgroundResource(R.drawable.example_2_selected_bg)
                            }
                            today -> {
                                textView.setTextColorRes(R.color.md_theme_error)
                                textView.background = null
                            }
                            else -> {
                                textView.setTextColorRes(R.color.md_theme_scrim)
                                textView.background = null
                            }
                        }
                    } else {
                        textView.makeInVisible()
                    }
                }
            }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = Example2CalendarHeaderBinding.bind(view).exTwoHeaderText
        }
        calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)

                override fun bind(
                    container: MonthViewContainer,
                    data: CalendarMonth,
                ) {
                    container.textView.text = data.yearMonth.displayText()
                }
            }
    }

    fun setOnDateSelectedListenerDepart(listener: OnDateSelectedListener) {
        this.onDateSelectedListener = listener
    }
}
