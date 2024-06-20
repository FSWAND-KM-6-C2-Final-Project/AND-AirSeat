package com.nafi.airseat.presentation.resetpasswordverifyemail

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResetPasswordEmailBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.otpresetpassword.OtpResetPasswordActivity
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.showSnackBarError
import com.nafi.airseat.utils.showSnackBarSuccess
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReqChangePasswordActivity : AppCompatActivity() {
    private val binding: ActivityResetPasswordEmailBinding by lazy {
        ActivityResetPasswordEmailBinding.inflate(layoutInflater)
    }
    private val reqChangePasswordViewModel: ReqChangePasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnVerify.setOnClickListener {
            reqChangePasswordByEmail()
        }
        binding.icDetailBackButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToOtpResetPassword(email: String) {
        startActivity(
            Intent(this, OtpResetPasswordActivity::class.java).apply {
                putExtra("email", email)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun reqChangePasswordByEmail() {
        if (isFormValid()) {
            val email = binding.layoutFormResetPasswordEmail.etEmail.text.toString().trim()
            proceedResetPasswordEmail(email)
        }
    }

    private fun proceedResetPasswordEmail(email: String) {
        reqChangePasswordViewModel.reqChangePasswordByEmail(email).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnVerify.isVisible = true
                    navigateToOtpResetPassword(email)
                    showSnackBarSuccess(getString(R.string.text_verify_change_password_success))
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnVerify.isVisible = true
                    if (it.exception is ApiErrorException) {
                        showSnackBarError("${it.exception.errorResponse.message}")
                    } else if (it.exception is NoInternetException) {
                        showSnackBarError(getString(R.string.text_no_internet))
                    }
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnVerify.isVisible = false
                },
            )
        }
    }

    private fun setupForm() {
        with(binding.layoutFormResetPasswordEmail) {
            tilEmail.isVisible = true
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.layoutFormResetPasswordEmail.etEmail.text.toString().trim()
        return checkEmailValidation(email)
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutFormResetPasswordEmail.tilEmail.isErrorEnabled = true
            binding.layoutFormResetPasswordEmail.tilEmail.error =
                getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormResetPasswordEmail.tilEmail.isErrorEnabled = true
            binding.layoutFormResetPasswordEmail.tilEmail.error =
                getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutFormResetPasswordEmail.tilEmail.isErrorEnabled = false
            true
        }
    }
}
