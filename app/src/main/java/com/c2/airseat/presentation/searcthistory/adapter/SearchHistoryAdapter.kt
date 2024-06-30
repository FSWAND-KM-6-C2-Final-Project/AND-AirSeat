package com.c2.airseat.presentation.searcthistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.c2.airseat.data.model.SearchHistory
import com.c2.airseat.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter(private val itemClick: (SearchHistory) -> Unit) :
    RecyclerView.Adapter<SearchHistoryAdapter.ItemSearchHistoryViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<SearchHistory>() {
                override fun areItemsTheSame(
                    oldItem: SearchHistory,
                    newItem: SearchHistory,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SearchHistory,
                    newItem: SearchHistory,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun insertData(data: List<SearchHistory>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemSearchHistoryViewHolder {
        val binding =
            ItemSearchHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ItemSearchHistoryViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: ItemSearchHistoryViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    class ItemSearchHistoryViewHolder(
        private val binding: ItemSearchHistoryBinding,
        private val itemClick: (SearchHistory) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchHistory) {
            binding.tvRecentHistory.text = data.searchHistory
            binding.ivDeleteSingleHistory.setOnClickListener { itemClick(data) }
        }
    }
}
