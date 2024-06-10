package com.nafi.airseat.presentation.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.data.model.NotificationModel
import com.nafi.airseat.databinding.ItemNotificationBinding
import com.nafi.airseat.utils.toFormattedDateNotification

class NotificationAdapter(private val itemClick: (NotificationModel) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.ItemNotificationViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<NotificationModel>() {
                override fun areItemsTheSame(
                    oldItem: NotificationModel,
                    newItem: NotificationModel,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: NotificationModel,
                    newItem: NotificationModel,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun insertData(data: List<NotificationModel>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemNotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ItemNotificationViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: ItemNotificationViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    class ItemNotificationViewHolder(
        private val binding: ItemNotificationBinding,
        private val itemClick: (NotificationModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationModel) {
            binding.tvNotificationType.text = data.notificationType
            binding.tvNotificationTitle.text = data.notificationTitle
            binding.tvNotificationDate.text = data.updatedAt.toFormattedDateNotification()
            itemView.setOnClickListener { itemClick(data) }
        }
    }
}
