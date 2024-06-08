package com.nafi.airseat.presentation.otpresetpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.databinding.ActivityOtpResetPasswordBinding
import com.nafi.airseat.presentation.resetpassword.ResetPasswordActivity
import com.nafi.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpResetPasswordBinding

    private val otpResetPasswordViewModel: OtpResetPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()

        val email = intent.getStringExtra("email")
        binding.textEmail.text = email

        binding.otpview.setText("")
        binding.otpview.setOtpCompletionListener { code ->
            hidekeyboard()

            if (email != null) {
                navigateToResetPassword(code, email)
            }
        }
    }

    private fun hidekeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE)as InputMethodManager

        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun setClickListeners() {
        binding.icDetailBackButton.setOnClickListener {
            navigateToResetPasswordEmail()
        }
        binding.textNewCodeOTP.setOnClickListener {
            val email = binding.textEmail.text.toString()
            resendOtpChangePassword(email)
        }
    }

    private fun navigateToResetPasswordEmail() {
        startActivity(
            Intent(this, ReqChangePasswordActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun resendOtpChangePassword(email: String) {
        otpResetPasswordViewModel.reqChangePasswordByEmailResendOtp(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.textNewCodeOTP.isVisible = true
                    Toast.makeText(
                        this,
                        "OTP sent to $email",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnError = {
                    binding.textNewCodeOTP.isVisible = true
                    Toast.makeText(
                        this,
                        "Failed to send OTP: ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    binding.textNewCodeOTP.isVisible = false
                },
            )
        }
    }

    private fun navigateToResetPassword(
        code: String,
        email: String,
    ) {
        startActivity(
            Intent(this, ResetPasswordActivity::class.java).apply { // Replace with actual target activity
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("code", code)
                putExtra("email", email)
            },
        )
    }
    /*private fun verifyOtpChangePassword(email: String) {
        otpResetPasswordViewModel.reqChangePasswordByEmailResendOtp(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = { code ->
                    Toast.makeText(
                        this,
                        "OTP verification successful",
                        Toast.LENGTH_SHORT,
                    ).show()
                    navigateToResetPassword(email, code.toString()) // Replace with actual navigation
                },
                doOnError = {
                    Toast.makeText(
                        this,
                        "OTP verification failed: ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    // Show loading indicator if needed
                },
            )
        }
    }*/
}
