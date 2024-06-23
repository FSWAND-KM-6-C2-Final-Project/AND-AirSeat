package com.nafi.airseat.presentation.departcalendar

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.nafi.airseat.R
import com.nafi.airseat.databinding.FragmentDepartCalendarBinding
import com.nafi.airseat.databinding.ItemSingleDayBinding
import com.nafi.airseat.utils.calendar.DateSelection
import com.nafi.airseat.utils.calendar.displayText
import com.nafi.airseat.utils.makeInVisible
import com.nafi.airseat.utils.makeVisible
import com.nafi.airseat.utils.setTextColorRes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class DepartCalendarFragment(private val isStartSelection: Boolean) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDepartCalendarBinding

    private var listener: OnDateDepartSelectedListener? = null

    private var selection = DateSelection()

    private val today = LocalDate.now()

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE, d MMM y")

    interface OnDateDepartSelectedListener {
        fun onDateDepartSelected(selectedDepart: LocalDate?)
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
        configureBinders()
        listener?.let { listener ->
            val selectedDate = selection.startDate
            if (selectedDate != null) {
                listener.onDateDepartSelected(selectedDate)
            }
        }
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)

        val titlesContainer = view.findViewById<ViewGroup>(R.id.titlesContainer)
        titlesContainer.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }

        binding.calendarView.monthScrollListener = { month ->
            binding.tvTitleMonth.text = month.yearMonth.displayText()
        }

        binding.ivNextMonth.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        binding.ivPreviousMonth.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }

        binding.btnSaveDate.setOnClickListener {
            val selectedDate = selection.startDate
            listener?.onDateDepartSelected(selectedDate)
            dismiss()
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        bindSummaryViews()
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun setOnDateDepartSelectedListener(listener: OnDateDepartSelectedListener) {
        this.listener = listener
    }

    private fun bindSummaryViews() {
        binding.tvTitleDeparture.apply {
            val selectedDate = selection.startDate
            if (selectedDate != null) {
                text = headerDateFormatter.format(selectedDate)
            } else {
                text = getString(R.string.start_date)
            }
        }

        binding.btnSaveDate.isEnabled = selection.startDate != null
    }

    private fun configureBinders() {
        val singleBackground = ContextCompat.getDrawable(requireContext(), R.drawable.calendar_single_selected_bg)
        val todayBackground = ContextCompat.getDrawable(requireContext(), R.drawable.calendar_today_bg)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = ItemSingleDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate &&
                        (day.date == today || day.date.isAfter(today))
                    ) {
                        selection.startDate = day.date
                        this@DepartCalendarFragment.binding.calendarView.notifyCalendarChanged()
                        bindSummaryViews()
                    }
                }
            }
        }

        binding.calendarView.dayBinder =
            object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(
                    container: DayViewContainer,
                    data: CalendarDay,
                ) {
                    container.day = data
                    val textView = container.binding.calendarDayText
                    val roundBgView = container.binding.RoundBackgroundView

                    textView.text = null
                    roundBgView.makeInVisible()

                    val selectedDate = selection.startDate

                    when (data.position) {
                        DayPosition.MonthDate -> {
                            textView.text = data.date.dayOfMonth.toString()
                            if (data.date.isBefore(today)) {
                                textView.setTextColorRes(R.color.md_theme_outlineVariant)
                            } else {
                                if (selectedDate == data.date) {
                                    textView.setTextColorRes(R.color.md_theme_surface)
                                    if (singleBackground != null) {
                                        roundBgView.applyBackground(singleBackground)
                                    }
                                } else if (data.date == today) {
                                    textView.setTextColorRes(R.color.md_theme_scrim)
                                    if (todayBackground != null) {
                                        roundBgView.applyBackground(todayBackground)
                                    }
                                } else {
                                    textView.setTextColorRes(R.color.md_theme_scrim)
                                }
                            }
                        }
                        else -> {
                            textView.setTextColorRes(R.color.md_theme_outlineVariant)
                        }
                    }
                }

                private fun View.applyBackground(drawable: Drawable) {
                    makeVisible()
                    background = drawable
                }
            }
    }
}
