package com.c2.airseat.presentation.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.c2.airseat.R
import com.c2.airseat.data.model.NotificationModel
import com.c2.airseat.databinding.FragmentNotificationBinding
import com.c2.airseat.presentation.common.views.ContentState
import com.c2.airseat.presentation.detailnotification.DetailNotificationActivity
import com.c2.airseat.presentation.login.LoginActivity
import com.c2.airseat.presentation.notification.adapter.NotificationAdapter
import com.c2.airseat.utils.ApiErrorException
import com.c2.airseat.utils.NoInternetException
import com.c2.airseat.utils.proceedWhen
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
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.layoutLoginProtectionNotification.btnLogin.setOnClickListener {
            navigateToLogin()
        }
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

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
