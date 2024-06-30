package com.c2.airseat.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.c2.airseat.R
import com.c2.airseat.data.model.Flight
import com.c2.airseat.databinding.LayoutFavoriteDestinationBinding
import com.c2.airseat.utils.toCurrencyFormat
import com.c2.airseat.utils.toFormatDateCommon

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
                val context = binding.root.context
                binding.tvDepartFavorite.text =
                    context.getString(
                        R.string.text_depart_arrival_favorite,
                        item.departureAirport.airportCity,
                        item.arrivalAirport.airportCity,
                    )
                binding.tvAirlinesFavorite.text = item.airline.airlineName
                binding.tvDurationFavorite.text = item.departureTime.toFormatDateCommon()
                binding.tvPriceFavorite.text =
                    context.getString(
                        R.string.text_price_favorite_start_from,
                        item.priceEconomy.toLong().toCurrencyFormat(),
                    )
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}
