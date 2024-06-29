package com.nafi.airseat.presentation.detailnotification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.data.model.NotificationModel
import com.nafi.airseat.databinding.ActivityDetailNotificationBinding
import com.nafi.airseat.utils.toFormattedDateNotification

class DetailNotificationActivity : AppCompatActivity() {
    companion object {
        const val EXTRAS_DETAIL_DATA = "EXTRAS_DETAIL_DATA"

        fun startActivity(
            context: Context,
            data: NotificationModel,
        ) {
            val intent = Intent(context, DetailNotificationActivity::class.java)
            intent.putExtra(EXTRAS_DETAIL_DATA, data)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityDetailNotificationBinding by lazy {
        ActivityDetailNotificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getIntentData()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.ivArrowBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getIntentData() {
        intent.extras?.getParcelable<NotificationModel>(EXTRAS_DETAIL_DATA)?.let {
            if (it.notificationType == "Notification") {
                binding.tvTermAndService.isVisible = false
            }
            binding.tvTypeNotification.text = it.notificationType
            binding.tvDateNotification.text = it.updatedAt.toFormattedDateNotification()
            binding.tvContentNotification.text = it.notificationDescription
        }
    }
}
