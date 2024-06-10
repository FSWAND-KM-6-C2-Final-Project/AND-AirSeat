package com.nafi.airseat.presentation.detailnotification

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.NotificationModel

class DetailNotificationViewModel(
    private val extras: Bundle?,
) : ViewModel() {
    private val notificationData =
        extras?.getParcelable<NotificationModel>(DetailNotificationActivity.EXTRAS_DETAIL_DATA)
}
