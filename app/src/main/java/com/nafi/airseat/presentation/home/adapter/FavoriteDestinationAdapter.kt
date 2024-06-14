package com.nafi.airseat.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nafi.airseat.data.model.FavoriteDestination
import com.nafi.airseat.databinding.LayoutFavoriteDestinationBinding

class FavoriteDestinationAdapter(private val listener: (FavoriteDestination) -> Unit) :
    RecyclerView.Adapter<FavoriteDestinationAdapter.ItemFavoriteDestinationViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<FavoriteDestination>() {
                override fun areItemsTheSame(
                    oldItem: FavoriteDestination,
                    newItem: FavoriteDestination,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: FavoriteDestination,
                    newItem: FavoriteDestination,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<FavoriteDestination>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemFavoriteDestinationViewHolder {
        val binding = LayoutFavoriteDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFavoriteDestinationViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ItemFavoriteDestinationViewHolder,
        position: Int,
    ) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemFavoriteDestinationViewHolder(
        private val binding: LayoutFavoriteDestinationBinding,
        val itemClick: (FavoriteDestination) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: FavoriteDestination) {
            with(item) {
                binding.ivFavoriteImage.load(item.img) {
                    crossfade(true)
                }
                binding.tvDepartFavorite.text = item.departDestination
                binding.tvAirlinesFavorite.text = item.airline // Only temporary
                binding.tvDurationFavorite.text = item.duration
                binding.tvPriceFavorite.text = "$${item.price}"
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}
