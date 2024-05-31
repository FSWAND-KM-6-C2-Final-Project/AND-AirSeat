package com.nafi.airseat.presentation.bottomsheetcalendar

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import com.nafi.airseat.databinding.FragmentBottomSheetCalendarBinding
import com.nafi.airseat.databinding.ItemDayBinding
import com.nafi.airseat.databinding.ItemHeaderCalendarBinding
import com.nafi.airseat.utils.calendar.ContinuousSelectionHelper.getSelection
import com.nafi.airseat.utils.calendar.ContinuousSelectionHelper.isInDateBetweenSelection
import com.nafi.airseat.utils.calendar.ContinuousSelectionHelper.isOutDateBetweenSelection
import com.nafi.airseat.utils.calendar.DateSelection
import com.nafi.airseat.utils.calendar.dateRangeDisplayText
import com.nafi.airseat.utils.calendar.displayText
import com.nafi.airseat.utils.getDrawableCompat
import com.nafi.airseat.utils.makeInVisible
import com.nafi.airseat.utils.makeVisible
import com.nafi.airseat.utils.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class BottomSheetCalendarFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCalendarBinding
    private val viewModel: BottomSheetCalendarViewModel by viewModels()

    private val today = LocalDate.now()
    private var selection = DateSelection()
    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE'\n'd MMM")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BottomSheetCalendar", "onViewCreated called")
        setupCalendar()
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupCalendar() {
        val daysOfWeek = daysOfWeek()
        Log.d("BottomSheetCalendar", "Days of Week: $daysOfWeek")

        binding.legendLayout.root.children.forEachIndexed { index, child ->
            (child as TextView).apply {
                text = daysOfWeek[index].displayText()
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColorRes(R.color.md_theme_onTertiaryFixedVariant)
            }
        }

        configureBinders()
        val currentMonth = YearMonth.now()
        Log.d("BottomSheetCalendar", "Current Month: $currentMonth")
        binding.exFourCalendar.setup(
            currentMonth,
            currentMonth.plusMonths(12),
            daysOfWeek.first(),
        )
        binding.exFourCalendar.scrollToMonth(currentMonth)
        bindSummaryViews()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener click@{
            val (startDate, endDate) = selection
            if (startDate != null && endDate != null) {
                val text = dateRangeDisplayText(startDate, endDate)
                Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
            }
            dismiss()
        }
    }

    private fun bindSummaryViews() {
        binding.exFourStartDateText.apply {
            if (selection.startDate != null) {
                text = headerDateFormatter.format(selection.startDate)
                setTextColorRes(R.color.md_theme_onTertiaryFixedVariant)
            } else {
                text = getString(R.string.start_date)
                setTextColor(Color.GRAY)
            }
        }

        binding.exFourEndDateText.apply {
            if (selection.endDate != null) {
                text = headerDateFormatter.format(selection.endDate)
                setTextColorRes(R.color.md_theme_onTertiaryFixedVariant)
            } else {
                text = getString(R.string.end_date)
                setTextColor(Color.GRAY)
            }
        }

        binding.btnSave.isEnabled = selection.daysBetween != null
    }

    private fun configureBinders() {
        val clipLevelHalf = 5000
        val ctx = requireContext()
        val rangeStartBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_start).also {
                it.level = clipLevelHalf
            }
        val rangeEndBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_end).also {
                it.level = clipLevelHalf
            }
        val rangeMiddleBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_middle)
        val singleBackground = ctx.getDrawableCompat(R.drawable.example_4_single_selected_bg)
        val todayBackground = ctx.getDrawableCompat(R.drawable.example_4_today_bg)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = ItemDayBinding.bind(view)

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
                        this@BottomSheetCalendarFragment.binding.exFourCalendar.notifyCalendarChanged()
                        bindSummaryViews()
                    }
                }
            }
        }

        binding.exFourCalendar.dayBinder =
            object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(
                    container: DayViewContainer,
                    data: CalendarDay,
                ) {
                    container.day = data
                    val textView = container.binding.exFourDayText
                    val roundBgView = container.binding.exFourRoundBackgroundView
                    val continuousBgView = container.binding.exFourContinuousBackgroundView

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
                                        textView.setTextColorRes(R.color.md_theme_onPrimary)
                                        roundBgView.applyBackground(singleBackground)
                                    }
                                    data.date == startDate -> {
                                        textView.setTextColorRes(R.color.md_theme_onPrimary)
                                        continuousBgView.applyBackground(rangeStartBackground)
                                        roundBgView.applyBackground(singleBackground)
                                    }
                                    startDate != null && endDate != null && (data.date > startDate && data.date < endDate) -> {
                                        textView.setTextColorRes(R.color.md_theme_onTertiaryFixedVariant)
                                        continuousBgView.applyBackground(rangeMiddleBackground)
                                    }
                                    data.date == endDate -> {
                                        textView.setTextColorRes(R.color.md_theme_onPrimary)
                                        continuousBgView.applyBackground(rangeEndBackground)
                                        roundBgView.applyBackground(singleBackground)
                                    }
                                    data.date == today -> {
                                        textView.setTextColorRes(R.color.md_theme_onTertiaryFixedVariant)
                                        roundBgView.applyBackground(todayBackground)
                                    }
                                    else -> textView.setTextColorRes(R.color.md_theme_onTertiaryFixedVariant)
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

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = ItemHeaderCalendarBinding.bind(view).exFourHeaderText
        }
        binding.exFourCalendar.monthHeaderBinder =
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
}
