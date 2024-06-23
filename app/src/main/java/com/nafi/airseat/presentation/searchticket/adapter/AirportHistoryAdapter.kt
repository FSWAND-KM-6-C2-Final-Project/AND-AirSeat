package com.nafi.airseat.presentation.searchticket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.data.model.AirportHistory
import com.nafi.airseat.databinding.ItemHistorySearchAirportBinding

class AirportHistoryAdapter(private val listener: (AirportHistory) -> Unit) :
    RecyclerView.Adapter<AirportHistoryAdapter.ItemAirportHistoryViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<AirportHistory>() {
                override fun areItemsTheSame(
                    oldItem: AirportHistory,
                    newItem: AirportHistory,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: AirportHistory,
                    newItem: AirportHistory,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun insertData(data: List<AirportHistory>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemAirportHistoryViewHolder {
        val binding =
            ItemHistorySearchAirportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ItemAirportHistoryViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: ItemAirportHistoryViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    class ItemAirportHistoryViewHolder(
        private val binding: ItemHistorySearchAirportBinding,
        private val itemClick: (AirportHistory) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AirportHistory) {
            binding.tvRecentAirport.text = data.airportHistory
            binding.ivDeleteSingleAirport.setOnClickListener { itemClick(data) }
        }
    }
}
