package com.c2.airseat.presentation.resultsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.c2.airseat.R
import com.c2.airseat.data.model.Flight
import com.c2.airseat.databinding.ItemResultSearchTicketBinding
import com.c2.airseat.utils.toCurrencyFormat
import com.c2.airseat.utils.toTimeClock

class ResultSearchAdapter(private val typeSeatClass: String?, private val listener: (Flight, Int) -> Unit) :
    RecyclerView.Adapter<ResultSearchAdapter.ItemFlightViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Flight>() {
                override fun areItemsTheSame(
                    oldItem: Flight,
                    newItem: Flight,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Flight,
                    newItem: Flight,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Flight>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemFlightViewHolder {
        val binding = ItemResultSearchTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFlightViewHolder(binding, listener, typeSeatClass)
    }

    override fun onBindViewHolder(
        holder: ItemFlightViewHolder,
        position: Int,
    ) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemFlightViewHolder(
        private val binding: ItemResultSearchTicketBinding,
        val itemClick: (Flight, Int) -> Unit,
        val typeSeatClass: String?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Flight) {
            with(item) {
                binding.tvDeparturePlace.text = item.departureAirport.airportCityCode
                binding.tvArrivalPlace.text = item.arrivalAirport.airportCityCode
                binding.tvDepartureTime.text = item.departureTime.toTimeClock()
                binding.tvDuration.text = item.duration
                binding.tvArrivalTime.text = item.arrivalTime.toTimeClock()
                binding.imgAirlineLogo.load(item.airline.airlinePicture) {
                    crossfade(true)
                }
                val context = binding.root.context
                binding.tvAirlineInfo.text =
                    context.getString(
                        R.string.text_result_airline_seat_class,
                        item.airline.airlineName,
                        typeSeatClass,
                    )
                val price: Long? =
                    when (typeSeatClass) {
                        "Economy" -> item.priceEconomy.toLong()
                        "Premium Economy" -> item.pricePremiumEconomy.toLong()
                        "Business" -> item.priceBusiness.toLong()
                        "First Class" -> item.priceFirstClass.toLong()
                        else -> null
                    }

                if (price != null) {
                    binding.tvTicketPrice.text = price.toCurrencyFormat()
                }
                itemView.setOnClickListener {
                    if (price != null) {
                        itemClick(this, price.toInt())
                    }
                }
            }
        }
    }
}
