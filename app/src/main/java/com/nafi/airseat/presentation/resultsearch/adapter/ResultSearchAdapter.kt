package com.nafi.airseat.presentation.resultsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nafi.airseat.data.model.Flight
import com.nafi.airseat.databinding.ItemResultSearchTicketBinding

class ResultSearchAdapter(private val listener: (Flight) -> Unit) :
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
        return ItemFlightViewHolder(binding, listener)
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
        val itemClick: (Flight) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Flight) {
            with(item) {
                binding.tvDeparturePlace.text = item.departureAirport.airportCityCode
                binding.tvArrivalPlace.text = item.arrivalAirport.airportCityCode
                // binding.tvDepartureTime.text = item.departureTime
                // binding.tvArrivalTime.text = item.arrivalTime
                binding.imgAirlineLogo.load(item.airline.airlinePicture) {
                    crossfade(true)
                }
                binding.tvAirlineInfo.text = item.airline.airlineName
                binding.tvTicketPrice.text = item.pricePremiumEconomy.toString()
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}
