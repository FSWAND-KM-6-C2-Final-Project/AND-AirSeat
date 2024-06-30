package com.c2.airseat.presentation.history.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import com.c2.airseat.R
import com.c2.airseat.data.model.History
import com.c2.airseat.databinding.ItemCardHistoryBinding
import com.c2.airseat.databinding.ItemMonthHistoryBinding
import com.c2.airseat.utils.capitalizeFirstLetter
import com.c2.airseat.utils.toCompleteDateFormat
import com.c2.airseat.utils.toCurrencyFormat
import com.c2.airseat.utils.toSeatClassNameMultiLine
import com.c2.airseat.utils.toTimeFormat
import com.xwray.groupie.viewbinding.BindableItem

class MonthHeaderItem(private val month: String) : BindableItem<ItemMonthHistoryBinding>() {
    override fun bind(
        viewBinding: ItemMonthHistoryBinding,
        position: Int,
    ) {
        viewBinding.tvMonthHistory.text = month
    }

    override fun getLayout() = R.layout.item_month_history

    override fun initializeViewBinding(view: View): ItemMonthHistoryBinding {
        return ItemMonthHistoryBinding.bind(view)
    }
}

class HistoryDataItem(
    private val data: History,
    private val onClick: (History) -> Unit,
) : BindableItem<ItemCardHistoryBinding>() {
    override fun bind(
        viewBinding: ItemCardHistoryBinding,
        position: Int,
    ) {
        setBackgroundStatus(data.bookingStatus, viewBinding)
        viewBinding.tvHistoryStatus.text = data.bookingStatus.capitalizeFirstLetter()
        viewBinding.tvLocationFrom.text = data.flight.departureAirport.airportCity
        viewBinding.tvLocationDestination.text = data.flight.arrivalAirport.airportCity
        viewBinding.tvDate.text = data.flight.departureTime.toCompleteDateFormat()
        viewBinding.tvDateDestination.text = data.flight.arrivalTime.toCompleteDateFormat()
        viewBinding.tvTime.text = data.flight.departureTime.toTimeFormat()
        viewBinding.tvTimeDestination.text = data.flight.arrivalTime.toTimeFormat()
        viewBinding.tvJourneyTime.text = data.duration
        viewBinding.bookingCode.text = data.bookingCode
        viewBinding.classSeat.text = data.classes.toSeatClassNameMultiLine()
        viewBinding.tvTotalHistory.text = data.totalAmount.toCurrencyFormat()
        viewBinding.root.setOnClickListener {
            onClick(data)
        }
    }

    override fun getLayout() = R.layout.item_card_history

    override fun initializeViewBinding(view: View): ItemCardHistoryBinding {
        return ItemCardHistoryBinding.bind(view)
    }

    private fun setBackgroundStatus(
        status: String,
        viewBinding: ItemCardHistoryBinding,
    ) {
        if (status == "issued") {
            viewBinding.tvHistoryStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#73CA5C"))
        } else if (status == "unpaid") {
            viewBinding.tvHistoryStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF0000"))
        }
    }
}
