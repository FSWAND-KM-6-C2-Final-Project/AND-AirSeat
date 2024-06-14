package com.nafi.airseat.presentation.searchticket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.databinding.ItemSearchAirportBinding
import java.util.Locale

class AirportsAdapter(private val listener: (Airport) -> Unit) :
    RecyclerView.Adapter<AirportsAdapter.ItemAirportsViewHolder>(), Filterable {
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: MutableList<Airport> = ArrayList()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(airportsFull)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    for (item in airportsFull) {
                        if (item.airportCity.lowercase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?,
            ) {
                if (results != null && results.values is List<*>) {
                    dataDiffer.submitList(results.values as List<Airport>)
                } else {
                    dataDiffer.submitList(airportsFull)
                }
            }
        }
    }

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
