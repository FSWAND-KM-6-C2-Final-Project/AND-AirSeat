package com.nafi.airseat.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.nafi.airseat.R
import com.nafi.airseat.data.model.NotificationModel
import com.nafi.airseat.databinding.FragmentNotificationBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.detailnotification.DetailNotificationActivity
import com.nafi.airseat.presentation.notification.adapter.NotificationAdapter
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {
    private val notificationViewModel: NotificationViewModel by viewModel()
    private lateinit var binding: FragmentNotificationBinding
    private var typeNotification: String? = null
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    private fun setAdapter() {
        adapter =
            NotificationAdapter(typeNotification.orEmpty()) { data ->
                navigateToDetailNotification(data)
            }
        binding.rvNotification.adapter = adapter
    }

    private fun setData() {
        notificationViewModel.getNotification().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutNotification.isVisible = true
                    binding.csvNotification.setState(ContentState.LOADING)
                    binding.layoutLoginProtectionNotification.root.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutNotification.isVisible = true
                    binding.csvNotification.setState(ContentState.SUCCESS)
                    binding.layoutLoginProtectionNotification.root.isVisible = false
                    result.payload?.let {
                        it.firstOrNull()?.let { notification ->
                            typeNotification = notification.notificationType
                            setAdapter()
                        }
                        adapter.insertData(it)
                    }
                },
                doOnError = {
                    binding.layoutNotification.isVisible = false
                    if (it.exception is ApiErrorException) {
                        val errorBody = it.exception.errorResponse.message
                        if (errorBody == "jwt malformed" || errorBody == "Token not found!") {
                            binding.layoutLoginProtectionNotification.root.isVisible = true
                        } else {
                            binding.csvNotification.setState(ContentState.ERROR_GENERAL)
                        }
                    } else if (it.exception is NoInternetException) {
                        binding.csvNotification.setState(ContentState.ERROR_NETWORK)
                    }
                },
                doOnEmpty = {
                    binding.layoutNotification.isVisible = true
                    binding.layoutLoginProtectionNotification.root.isVisible = false
                    binding.csvNotification.setState(
                        ContentState.EMPTY,
                        title = getString(R.string.text_title_empty_notification),
                        desc = getString(R.string.text_desc_empy_notification),
                    )
                },
            )
        }
    }

    private fun navigateToDetailNotification(data: NotificationModel) {
        DetailNotificationActivity.startActivity(requireContext(), data)
    }
}
