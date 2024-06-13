package com.nafi.airseat.presentation.resetpasswordverifyemail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityResetPasswordEmailBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.otpresetpassword.OtpResetPasswordActivity
import com.nafi.airseat.utils.proceedWhen
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

    private fun navigateToReqChangePassword() {
        startActivity(
            Intent(this, ReqChangePasswordActivity::class.java).apply {
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
                    showSnackbarSuccess(getString(R.string.text_verify_change_password_success))
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnVerify.isVisible = true
                    showSnackbarError("Verify Change Password Failed : ${it.exception?.message.orEmpty()}")
                    navigateToReqChangePassword()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnVerify.isVisible = false
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
            binding.layoutFormResetPasswordEmail.tilEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormResetPasswordEmail.tilEmail.isErrorEnabled = true
            binding.layoutFormResetPasswordEmail.tilEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutFormResetPasswordEmail.tilEmail.isErrorEnabled = false
            true
        }
    }

    /*private fun sendOtp(email: String) {
        reqChangePasswordViewModel.reqChangePasswordByEmail(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnVerify.isVisible = true
                    Toast.makeText(
                        this,
                        "OTP sent to $email",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnVerify.isVisible = true
                    Toast.makeText(
                        this,
                        "Failed to send OTP: ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                    navigateToReqChangePassword()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnVerify.isVisible = false
                },
            )
        }
    }*/
}
