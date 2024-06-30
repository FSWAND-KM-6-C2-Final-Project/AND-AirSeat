package com.c2.airseat.presentation.searchticket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.c2.airseat.data.model.Airport
import com.c2.airseat.databinding.ItemSearchAirportBinding

class AirportsAdapter(private val listener: (Airport) -> Unit) :
    RecyclerView.Adapter<AirportsAdapter.ItemAirportsViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Airport>() {
                override fun areItemsTheSame(
                    oldItem: Airport,
                    newItem: Airport,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Airport,
                    newItem: Airport,
                ): Boolean {
                    return oldItem == newItem
                }
            },
        )

    private var airportsFull: List<Airport> = emptyList()

    fun submitData(data: List<Airport>) {
        airportsFull = ArrayList(data)
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemAirportsViewHolder {
        val binding =
            ItemSearchAirportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ItemAirportsViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ItemAirportsViewHolder,
        position: Int,
    ) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemAirportsViewHolder(
        private val binding: ItemSearchAirportBinding,
        val itemClick: (Airport) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Airport) {
            with(item) {
                binding.tvAirportName.text = item.airportCity
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}
