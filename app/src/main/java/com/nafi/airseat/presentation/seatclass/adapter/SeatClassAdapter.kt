package com.nafi.airseat.presentation.seatclass.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.R
import com.nafi.airseat.data.model.SeatClass
import com.nafi.airseat.databinding.ItemClassBinding

class SeatClassAdapter(private val listener: (SeatClass) -> Unit) :
    RecyclerView.Adapter<SeatClassAdapter.ItemSeatClassViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<SeatClass>() {
                override fun areItemsTheSame(
                    oldItem: SeatClass,
                    newItem: SeatClass,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SeatClass,
                    newItem: SeatClass,
                ): Boolean {
                    return oldItem == newItem
                }
            },
        )

    private var selectedItemPos = -1

    fun submitData(data: List<SeatClass>) {
        dataDiffer.submitList(data)
    }

    fun getSelectedSeatClass(): SeatClass? {
        return if (selectedItemPos != -1) {
            dataDiffer.currentList[selectedItemPos]
        } else {
            null
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemSeatClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSeatClassViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemSeatClassViewHolder,
        position: Int,
    ) {
        holder.bindView(dataDiffer.currentList[position], position)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    inner class ItemSeatClassViewHolder(private val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val previousItemPos = selectedItemPos
                selectedItemPos = adapterPosition
                notifyItemChanged(previousItemPos)
                notifyItemChanged(selectedItemPos)
                listener(dataDiffer.currentList[selectedItemPos])
            }
        }

        fun bindView(
            item: SeatClass,
            position: Int,
        ) {
            binding.optionText.text = item.seatName
            binding.priceTextEconomy.text = item.seatPrice.toString()
            updateBackground(position == selectedItemPos)
        }

        private fun updateBackground(isSelected: Boolean) {
            if (isSelected) {
                binding.root.background = ContextCompat.getDrawable(itemView.context, R.drawable.selected_item_background)
            } else {
                binding.root.background = ContextCompat.getDrawable(itemView.context, R.drawable.unselected_item_background)
            }
        }
    }
}
