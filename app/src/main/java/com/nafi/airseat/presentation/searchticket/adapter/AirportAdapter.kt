package com.nafi.airseat.presentation.searchticket.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.databinding.ItemSearchAirportBinding

class AirportAdapter(
    private var airportList: List<Airport>,
    private val listener: AirportClickListener,
) :
    RecyclerView.Adapter<AirportAdapter.AirportViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<Airport>) {
        airportList = filteredList
        notifyDataSetChanged()
    }

    interface AirportClickListener {
        fun onAirportClicked(airport: Airport)
    }

    private var airportClickListener: AirportClickListener? = null

    fun setAirportClickListener(listener: AirportClickListener?) {
        airportClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AirportViewHolder {
        val binding = ItemSearchAirportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AirportViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: AirportViewHolder,
        position: Int,
    ) {
        val airport = airportList[position]
        holder.bind(airport)
        // holder.itemView.setOnClickListener { listener(airport) }
    }

    override fun getItemCount(): Int {
        return airportList.size
    }

    class AirportViewHolder(
        private val binding: ItemSearchAirportBinding,
        val itemClick: AirportClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(airport: Airport) {
            binding.tvAirportName.text = airport.airportName
            itemView.setOnClickListener { itemClick.onAirportClicked(airport) }
        }
    }
}
