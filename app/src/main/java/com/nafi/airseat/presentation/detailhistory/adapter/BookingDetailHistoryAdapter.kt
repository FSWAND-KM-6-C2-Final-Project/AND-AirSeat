package com.nafi.airseat.presentation.detailhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.data.model.BookingDetailHistory
import com.nafi.airseat.databinding.ItemPassengerBinding
import com.nafi.airseat.utils.capitalizeFirstLetter

class PassengerAdapter() : RecyclerView.Adapter<PassengerAdapter.ItemPassengerViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<BookingDetailHistory>() {
                override fun areItemsTheSame(
                    oldItem: BookingDetailHistory,
                    newItem: BookingDetailHistory,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: BookingDetailHistory,
                    newItem: BookingDetailHistory,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun insertData(data: List<BookingDetailHistory>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemPassengerViewHolder {
        val binding =
            ItemPassengerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ItemPassengerViewHolder(binding)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: ItemPassengerViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    class ItemPassengerViewHolder(
        private val binding: ItemPassengerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BookingDetailHistory) {
            binding.tvPassengerTitle.text = data.passenger.title?.capitalizeFirstLetter()
            binding.tvPassengerFirstName.text = data.passenger.firstName?.capitalizeFirstLetter()
            binding.tvPassengerLastName.text = data.passenger.lastName?.capitalizeFirstLetter()
            binding.tvIdPassenger.text = data.passenger.id.toString()
        }
    }
}
