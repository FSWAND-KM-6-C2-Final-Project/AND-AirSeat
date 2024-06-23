package com.nafi.airseat.presentation.calendar

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.nafi.airseat.databinding.FragmentCalendarBottomSheetBinding
import com.nafi.airseat.databinding.ItemSingleDayBinding
import com.nafi.airseat.utils.calendar.ContinuousSelectionHelper.getSelection
import com.nafi.airseat.utils.calendar.ContinuousSelectionHelper.isInDateBetweenSelection
import com.nafi.airseat.utils.calendar.ContinuousSelectionHelper.isOutDateBetweenSelection
import com.nafi.airseat.utils.calendar.DateSelection
import com.nafi.airseat.utils.calendar.displayText
import com.nafi.airseat.utils.calendar.getDrawableCompat
import com.nafi.airseat.utils.calendar.makeInVisible
import com.nafi.airseat.utils.calendar.makeVisible
import com.nafi.airseat.utils.calendar.setTextColorRes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class CalendarBottomSheetFragment(private val isStartSelection: Boolean) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCalendarBottomSheetBinding

    private var listener: OnDateSelectedListener? = null

    private var selection = DateSelection()

    private val today = LocalDate.now()

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE, d MMM y")

    interface OnDateSelectedListener {
        fun onDateSelected(
            startDate: LocalDate?,
            endDate: LocalDate?,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCalendarBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        configureBinders()
        listener?.let { listener ->
            val (startDate, endDate) = selection
            if (startDate != null && endDate != null) {
                listener.onDateSelected(startDate, endDate)
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
            val (startDate, endDate) = selection
            listener?.onDateSelected(startDate, endDate)
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

    fun setOnDateSelectedListener(listener: OnDateSelectedListener) {
        this.listener = listener
    }

    private fun bindSummaryViews() {
        binding.tvStartDate.apply {
            if (selection.startDate != null) {
                text = headerDateFormatter.format(selection.startDate)
            } else {
                text = getString(R.string.start_date)
            }
        }

        binding.tvEndDate.apply {
            if (selection.endDate != null) {
                text = headerDateFormatter.format(selection.endDate)
            } else {
                text = getString(R.string.end_date)
            }
        }

        binding.btnSaveDate.isEnabled = selection.daysBetween != null
    }

    private fun configureBinders() {
        val clipLevelHalf = 5000
        val ctx = requireContext()
        val rangeStartBackground =
            ctx.getDrawableCompat(R.drawable.calendar_continuous_selected_bg_start).also {
                it.level = clipLevelHalf
            }
        val rangeEndBackground =
            ctx.getDrawableCompat(R.drawable.calendar_continuous_selected_bg_end).also {
                it.level = clipLevelHalf
            }
        val rangeMiddleBackground =
            ctx.getDrawableCompat(R.drawable.calendar_continuous_selected_bg_middle)
        val singleBackground = ctx.getDrawableCompat(R.drawable.calendar_single_selected_bg)
        val todayBackground = ctx.getDrawableCompat(R.drawable.calendar_today_bg)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = ItemSingleDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate &&
                        (day.date == today || day.date.isAfter(today))
                    ) {
                        selection =
                            getSelection(
                                clickedDate = day.date,
                                dateSelection = selection,
                            )
                        this@CalendarBottomSheetFragment.binding.calendarView.notifyCalendarChanged()
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
                    val continuousBgView = container.binding.ContinuousBackgroundView

                    textView.text = null
                    roundBgView.makeInVisible()
                    continuousBgView.makeInVisible()

                    val (startDate, endDate) = selection

                    when (data.position) {
                        DayPosition.MonthDate -> {
                            textView.text = data.date.dayOfMonth.toString()
                            if (data.date.isBefore(today)) {
                                textView.setTextColorRes(R.color.md_theme_outlineVariant)
                            } else {
                                when {
                                    startDate == data.date && endDate == null -> {
                                        textView.setTextColorRes(R.color.md_theme_surface)
                                        roundBgView.applyBackground(singleBackground)
                                    }

                                    data.date == startDate -> {
                                        textView.setTextColorRes(R.color.md_theme_surface)
                                        continuousBgView.applyBackground(rangeStartBackground)
                                        roundBgView.applyBackground(singleBackground)
                                    }

                                    startDate != null && endDate != null && (data.date > startDate && data.date < endDate) -> {
                                        textView.setTextColorRes(R.color.md_theme_outline)
                                        continuousBgView.applyBackground(rangeMiddleBackground)
                                    }

                                    data.date == endDate -> {
                                        textView.setTextColorRes(R.color.md_theme_surface)
                                        continuousBgView.applyBackground(rangeEndBackground)
                                        roundBgView.applyBackground(singleBackground)
                                    }

                                    data.date == today -> {
                                        textView.setTextColorRes(R.color.md_theme_scrim)
                                        roundBgView.applyBackground(todayBackground)
                                    }

                                    else -> textView.setTextColorRes(R.color.md_theme_scrim)
                                }
                            }
                        }
                        DayPosition.InDate ->
                            if (startDate != null && endDate != null &&
                                isInDateBetweenSelection(data.date, startDate, endDate)
                            ) {
                                continuousBgView.applyBackground(rangeMiddleBackground)
                            }

                        DayPosition.OutDate ->
                            if (startDate != null && endDate != null &&
                                isOutDateBetweenSelection(data.date, startDate, endDate)
                            ) {
                                continuousBgView.applyBackground(rangeMiddleBackground)
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
