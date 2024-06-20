package com.nafi.airseat.presentation.seatclass.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ItemClassBinding

class SeatClassAdapter(
    private var items: List<String>,
    private val listener: (String) -> Unit,
) : RecyclerView.Adapter<SeatClassAdapter.ItemSeatClassViewHolder>() {
    private var selectedPosition = RecyclerView.NO_POSITION

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
        holder.bind(items[position], position == selectedPosition)
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            listener(items[selectedPosition])
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ItemSeatClassViewHolder(private val binding: ItemClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: String,
            isSelected: Boolean,
        ) {
            binding.textView.text = item
            binding.textView.setTextColor(
                if (isSelected) {
                    binding.root.context.getColor(R.color.md_theme_onError)
                } else {
                    binding.root.context.getColor(R.color.md_theme_scrim)
                },
            )
            binding.llExample.setBackgroundResource(
                if (isSelected) {
                    R.color.md_theme_primary
                } else {
                    android.R.color.transparent
                },
            )
        }
    }
}
