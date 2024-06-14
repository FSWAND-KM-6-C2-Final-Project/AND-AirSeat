package com.nafi.airseat.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.nafi.airseat.core.BaseActivity
import com.nafi.airseat.databinding.FragmentProfileBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModel()

    // val token = (activity as BaseActivity).observeToken()
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
        setOptionMenu()
    }

    private fun observeDataProfile() {
        viewModel.getDataProfile().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_SHORT).show()
                },
                doOnSuccess = {
                    result.payload?.let {
                        Toast.makeText(requireContext(), it.fullName, Toast.LENGTH_SHORT).show()
                    }
                },
                doOnError = {
                    Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
                },
                doOnEmpty = {
                    Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun setOptionMenu() {
        (activity as BaseActivity).token.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.itemChangeProfile.isVisible = false
                binding.itemProfileSetting.isVisible = false
                binding.tvLogin.text = "Login"
            } else {
                binding.itemChangeProfile.isVisible = true
                binding.itemProfileSetting.isVisible = true
                binding.tvLogin.text = "Logout"
                observeDataProfile()
            }
        }
    }

    private fun setOnclickListener() {
        binding.itemLogout.setOnClickListener {
            (activity as BaseActivity).cleatToken()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
