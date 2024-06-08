package com.nafi.airseat.presentation.searchticket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.databinding.ItemSearchAirportBinding

class AirportAdapter(private val listener: (Airport) -> Unit) :
    RecyclerView.Adapter<AirportAdapter.ItemAirportViewHolder>() {
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
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Airport>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemAirportViewHolder {
        val binding = ItemSearchAirportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemAirportViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ItemAirportViewHolder,
        position: Int
    ) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemAirportViewHolder(
        private val binding: ItemSearchAirportBinding,
        val itemClick: (Airport) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Airport) {
            with(item) {
                binding.tvAirportName.text = airportName
                binding.root.setOnClickListener {
                    itemClick(this)
                }
            }
        }
    }
}