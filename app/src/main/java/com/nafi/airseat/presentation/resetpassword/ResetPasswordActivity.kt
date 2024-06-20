package com.nafi.airseat.presentation.resetpassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResetPasswordBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.otpresetpassword.OtpResetPasswordActivity
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.hideKeyboard
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.showSnackBarError
import com.nafi.airseat.utils.showSnackBarSuccess
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : AppCompatActivity() {
    private val binding: ActivityResetPasswordBinding by lazy {
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModel()
    private lateinit var code: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        code = intent.getStringExtra("code") ?: ""
        email = intent.getStringExtra("email") ?: ""

        setupForm()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnSave.setOnClickListener {
            hideKeyboard()
            reqChangePassword()
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun reqChangePassword() {
        if (isFormValid()) {
            val password = binding.layoutFormResetPassword.etPassword.text.toString().trim()
            val confirmPassword = binding.layoutFormResetPassword.etConfirmPassword.text.toString().trim()
            proceedResetPassword(code, email, password, confirmPassword)
        }
    }

    private fun proceedResetPassword(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        resetPasswordViewModel.verifyChangePasswordOtp(code, email, password, confirmPassword).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnSave.isVisible = true
                    showSnackBarSuccess(getString(R.string.text_change_password_success))
                    navigateToLogin()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnSave.isVisible = true
                    if (it.exception is ApiErrorException) {
                        showSnackBarError("${it.exception.errorResponse.message}")
                    } else if (it.exception is NoInternetException) {
                        showSnackBarError("No Internet, Please Check Your Connection")
                    }
                    navigateToOtpResetPassword(email)
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnSave.isVisible = false
                },
            )
        }
    }

    private fun setupForm() {
        with(binding.layoutFormResetPassword) {
            tilPassword.isVisible = true
            tilConfirmPassword.isVisible = true
        }
    }

    private fun isFormValid(): Boolean {
        val password = binding.layoutFormResetPassword.etPassword.text.toString().trim()
        val confirmPassword = binding.layoutFormResetPassword.etConfirmPassword.text.toString().trim()

        return checkPasswordValidation(password, binding.layoutFormResetPassword.tilPassword) &&
            checkPasswordValidation(confirmPassword, binding.layoutFormResetPassword.tilConfirmPassword) &&
            checkPwdAndConfirmPwd(password, confirmPassword)
    }

    private fun checkPasswordValidation(
        confirmPassword: String,
        textInputLayout: TextInputLayout,
    ): Boolean {
        return if (confirmPassword.isEmpty()) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error =
                getString(R.string.text_error_password_empty)
            false
        } else if (confirmPassword.length < 8) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error =
                getString(R.string.text_error_password_less_than_8_char)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun checkPwdAndConfirmPwd(
        password: String,
        confirmPassword: String,
    ): Boolean {
        return if (password != confirmPassword) {
            binding.layoutFormResetPassword.tilPassword.isErrorEnabled = true
            binding.layoutFormResetPassword.tilPassword.error =
                getString(R.string.text_password_does_not_match)
            binding.layoutFormResetPassword.tilConfirmPassword.isErrorEnabled = true
            binding.layoutFormResetPassword.tilConfirmPassword.error =
                getString(R.string.text_password_does_not_match)
            false
        } else {
            binding.layoutFormResetPassword.tilPassword.isErrorEnabled = false
            binding.layoutFormResetPassword.tilConfirmPassword.isErrorEnabled = false
            true
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
}
