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
                    binding.layoutProfile.isVisible = false
                    binding.csvProfile.setState(ContentState.LOADING)
                },
                doOnSuccess = {
                    result.payload?.let {
                        binding.layoutProfile.isVisible = true
                        binding.tvUserFullname.text = it.fullName
                        binding.tvUserEmail.text = it.email
                        binding.tvUserPhoneNumber.text = it.phoneNumber
                        btnToUpdateProfile(it.fullName)
                        binding.csvProfile.setState(ContentState.SUCCESS)
                    }
                },
                doOnError = {
                    navigateToLogin()
                },
                doOnEmpty = {
                    binding.layoutProfile.isVisible = false
                    binding.csvProfile.setState(ContentState.EMPTY)
                },
            )
        }
    }

    private fun setOnclickListener() {
        binding.itemLogout.setOnClickListener {
            (activity as BaseActivity).cleatToken()
            navigateToLogin()
        }
    }

    private fun btnToUpdateProfile(fullName: String) {
        binding.itemChangeProfile.setOnClickListener {
            navigateToUpdateProfile(fullName)
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateToUpdateProfile(fullName: String) {
        startActivity(
            Intent(requireContext(), UpdateProfileActivity::class.java).apply {
                putExtra("fullName", fullName)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
