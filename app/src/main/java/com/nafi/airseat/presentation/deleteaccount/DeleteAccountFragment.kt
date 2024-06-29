package com.nafi.airseat.presentation.deleteaccount

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.R
import com.nafi.airseat.core.BaseActivity
import com.nafi.airseat.databinding.FragmentDeleteAccountBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.showSnackBarError
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteAccountFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDeleteAccountBinding

    private val viewModel: DeleteAccountViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeleteAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setHeightBottomSheet()
        setButton()
        setOnClickListener()
    }

    private fun setButton() {
        binding.cbDeletion.setOnCheckedChangeListener { _, isChecked ->
            binding.btnDeleteAccount.isEnabled = isChecked
            if (isChecked) {
                binding.btnDeleteAccount.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red,
                    ),
                )
                binding.btnDeleteAccount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white,
                    ),
                )
            } else {
                binding.btnDeleteAccount.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray,
                    ),
                )
                binding.btnDeleteAccount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_gray,
                    ),
                )
            }
        }
    }

    private fun setOnClickListener() {
        binding.btnDeleteAccount.setOnClickListener {
            viewModel.deleteAccount().observe(viewLifecycleOwner) { result ->
                result.proceedWhen(
                    doOnLoading = {
                    },
                    doOnSuccess = {
                        (activity as BaseActivity).clearToken()
                        navigateToLogin()
                    },
                    doOnError = {
                        showSnackBarError("$it")
                    },
                )
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
        )
    }

    private fun setHeightBottomSheet() {
        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        val height = (screenHeight * 0.5).toInt()
        bottomSheet.layoutParams.height = height

        bottomSheet.layoutParams = bottomSheet.layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
