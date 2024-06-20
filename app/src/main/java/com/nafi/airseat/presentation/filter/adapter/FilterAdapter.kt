package com.nafi.airseat.presentation.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ItemFilterBinding

class FilterAdapter(
    private var items: List<String>,
    private val listener: (String) -> Unit,
) : RecyclerView.Adapter<FilterAdapter.ItemFilterViewHolder>() {
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemFilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFilterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemFilterViewHolder,
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

    class ItemFilterViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: String,
            isSelected: Boolean,
        ) {
            binding.tvFilter.text = item
            binding.tvFilter.setTextColor(
                if (isSelected) {
                    binding.root.context.getColor(R.color.md_theme_onError)
                } else {
                    binding.root.context.getColor(R.color.md_theme_scrim)
                },
            )
            binding.llFilter.setBackgroundResource(
                if (isSelected) {
                    R.color.md_theme_primary
                } else {
                    android.R.color.transparent
                },
            )
        }
    }
}
