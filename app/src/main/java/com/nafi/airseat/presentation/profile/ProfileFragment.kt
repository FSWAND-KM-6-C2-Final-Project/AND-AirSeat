package com.nafi.airseat.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.nafi.airseat.core.BaseActivity
import com.nafi.airseat.databinding.FragmentProfileBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.profilesetting.ProfileSettingActivity
import com.nafi.airseat.presentation.updateprofile.UpdateProfileActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.isVisible = false
        setOnclickListener()
    }

    override fun onResume() {
        super.onResume()
        observeDataProfile()
    }

    private fun observeDataProfile() {
        viewModel.getDataProfile().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.root.isVisible = true
                    binding.layoutProfile.isVisible = false
                    binding.csvProfile.setState(ContentState.LOADING)
                },
                doOnSuccess = {
                    binding.root.isVisible = true
                    result.payload?.let {
                        binding.layoutProfile.isVisible = true
                        binding.tvUserFullname.text = it.fullName
                        binding.tvUserEmail.text = it.email
                        binding.tvUserPhoneNumber.text = it.phoneNumber
                        btnToUpdateProfile(it.fullName, it.phoneNumber)
                        binding.csvProfile.setState(ContentState.SUCCESS)
                    }
                },
                doOnError = {
                    navigateToLogin()
                },
                doOnEmpty = {
                    binding.root.isVisible = true
                    binding.layoutProfile.isVisible = false
                    binding.csvProfile.setState(ContentState.EMPTY)
                },
            )
        }
    }

    private fun setOnclickListener() {
        binding.itemLogout.setOnClickListener {
            (activity as BaseActivity).clearToken()
            navigateToLogin()
        }
        binding.itemProfileSetting.setOnClickListener {
            navigateToDeleteAccount()
        }
    }

    private fun navigateToDeleteAccount() {
        startActivity(
            Intent(requireContext(), ProfileSettingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun btnToUpdateProfile(
        fullName: String,
        phoneNumber: String,
    ) {
        binding.itemChangeProfile.setOnClickListener {
            navigateToUpdateProfile(fullName, phoneNumber)
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
        )
    }

    private fun navigateToUpdateProfile(
        fullName: String,
        phoneNumber: String,
    ) {
        startActivity(
            Intent(requireContext(), UpdateProfileActivity::class.java).apply {
                putExtra("fullName", fullName)
                putExtra("phoneNumber", phoneNumber)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
