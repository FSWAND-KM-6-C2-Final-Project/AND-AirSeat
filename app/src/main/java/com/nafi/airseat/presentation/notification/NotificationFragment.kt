package com.nafi.airseat.presentation.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nafi.airseat.R
import com.nafi.airseat.data.model.NotificationModel
import com.nafi.airseat.databinding.FragmentNotificationBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.detailnotification.DetailNotificationActivity
import com.nafi.airseat.presentation.notification.adapter.NotificationAdapter
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {
    private val notificationViewModel: NotificationViewModel by viewModel()
    private lateinit var binding: FragmentNotificationBinding
    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter(typeNotification = typeNotification) { data ->
            navigateToDetailNotification(data)
        }
    }
    private var typeNotification: String? = null

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
        setAdapter()
        setData()
    }

    private fun setAdapter() {
        binding.rvNotification.adapter = this@NotificationFragment.adapter
    }

    private fun setData() {
        notificationViewModel.getNotification().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.csvNotification.setState(ContentState.LOADING)
                },
                doOnSuccess = {
                    binding.csvNotification.setState(ContentState.SUCCESS)
                    result.payload?.let {
                        adapter.insertData(it)
                        it.listIterator().forEach { type ->
                            typeNotification = type.notificationType
                            Log.d("Type", type.notificationType)
                        }
                    }
                },
                doOnError = {
                    if (it.exception is NoInternetException) {
                        binding.csvNotification.setState(ContentState.ERROR_NETWORK)
                    } else {
                        binding.csvNotification.setState(
                            ContentState.ERROR_GENERAL,
                            desc = result.exception?.message.orEmpty(),
                        )
                    }
                },
                doOnEmpty = {
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
