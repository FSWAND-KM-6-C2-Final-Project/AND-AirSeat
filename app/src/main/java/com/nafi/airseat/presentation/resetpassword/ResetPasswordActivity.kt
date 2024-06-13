package com.nafi.airseat.presentation.resetpassword

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResetPasswordBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordActivity
import com.nafi.airseat.utils.proceedWhen
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
            reqChangePassword()
        }
        binding.icDetailBackButton.setOnClickListener {
            navigateToReqChangePasswordByEmail()
        }
    }

    private fun navigateToReqChangePasswordByEmail() {
        startActivity(
            Intent(this, ReqChangePasswordActivity::class.java).apply {
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
                    showSnackbarSuccess(getString(R.string.text_change_password_success))
                    navigateToLogin()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnSave.isVisible = true
                    showSnackbarError(it.exception?.message.orEmpty())
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnSave.isVisible = false
                },
            )
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showSnackbarSuccess(message: String) {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar_success, null)
        customView.findViewById<TextView>(R.id.textView1).text = message
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customView, 0)
        snackbar.show()
    }

    @SuppressLint("RestrictedApi")
    private fun showSnackbarError(message: String) {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar_error, null)
        customView.findViewById<TextView>(R.id.textView1).text = message
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customView, 0)
        snackbar.show()
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
