package com.nafi.airseat.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nafi.airseat.data.model.Flight
import com.nafi.airseat.databinding.LayoutFavoriteDestinationBinding
import com.nafi.airseat.utils.toCurrencyFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FavoriteDestinationAdapter(private val listener: (Flight) -> Unit) :
    RecyclerView.Adapter<FavoriteDestinationAdapter.ItemFavoriteDestinationViewHolder>() {
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
    ): ItemFavoriteDestinationViewHolder {
        val binding = LayoutFavoriteDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFavoriteDestinationViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ItemFavoriteDestinationViewHolder,
        position: Int,
    ) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemFavoriteDestinationViewHolder(
        private val binding: LayoutFavoriteDestinationBinding,
        val itemClick: (Flight) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Flight) {
            with(item) {
                binding.ivFavoriteImage.load(item.arrivalAirport.airportPicture) {
                    crossfade(true)
                }
                binding.tvDepartFavorite.text = "${item.departureAirport.airportCity} - ${item.arrivalAirport.airportCity}"
                binding.tvAirlinesFavorite.text = item.airline.airlineName
                binding.tvDurationFavorite.text = formatDate(item.departureTime)
                binding.tvPriceFavorite.text = "Start from\n${item.priceEconomy.toLong().toCurrencyFormat()}"
                itemView.setOnClickListener { itemClick(this) }
            }
        }

        private fun formatDate(dateString: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateTime = LocalDateTime.parse(dateString, formatter)
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            return dateTime.format(outputFormatter)
        }
    }
}
