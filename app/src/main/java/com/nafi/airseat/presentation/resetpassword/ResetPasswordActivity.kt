package com.nafi.airseat.presentation.resetpassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResetPasswordBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.resetpasswordverifyemail.ResetPasswordEmailActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : AppCompatActivity() {
    private val binding: ActivityResetPasswordBinding by lazy {
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModel()

    private lateinit var email: String
    private lateinit var code: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        email = intent.getStringExtra("email") ?: ""
        code = intent.getStringExtra("code") ?: ""

        setupForm()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnSave.setOnClickListener {
            reqChangePassword()
        }
        binding.icDetailBackButton.setOnClickListener {
            navigateToReqChangePasswordByEmail()
        }
    }

    private fun navigateToReqChangePasswordByEmail() {
        startActivity(
            Intent(this, ResetPasswordEmailActivity::class.java).apply {
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
        resetPasswordViewModel.verifChangePasswordOtp(code, email, password, confirmPassword).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnSave.isVisible = true
                    Toast.makeText(
                        this,
                        getString(R.string.text_change_password_success),
                        Toast.LENGTH_SHORT,
                    ).show()
                    navigateToLogin()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnSave.isVisible = true
                    Toast.makeText(
                        this,
                        "Change Password Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
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
}
