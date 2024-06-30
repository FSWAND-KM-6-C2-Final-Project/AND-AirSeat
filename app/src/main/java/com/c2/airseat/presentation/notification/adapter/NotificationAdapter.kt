package com.c2.airseat.presentation.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.c2.airseat.data.model.NotificationModel
import com.c2.airseat.databinding.ItemNotificationBinding
import com.c2.airseat.utils.toFormattedDateNotification

class NotificationAdapter(private val typeNotification: String?, private val itemClick: (NotificationModel) -> Unit) :
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
        return ItemNotificationViewHolder(binding, itemClick, typeNotification)
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
        private val typeNotification: String?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationModel) {
            if (typeNotification == "Notification") {
                binding.tvNotificationFooter.isVisible = false
            }
            binding.tvNotificationType.text = data.notificationType
            binding.tvNotificationTitle.text = data.notificationTitle
            binding.tvNotificationDate.text = data.updatedAt.toFormattedDateNotification()
            itemView.setOnClickListener { itemClick(data) }
        }
    }
}
