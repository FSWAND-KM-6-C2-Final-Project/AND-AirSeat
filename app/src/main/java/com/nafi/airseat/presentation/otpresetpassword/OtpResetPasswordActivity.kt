package com.nafi.airseat.presentation.otpresetpassword

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityOtpResetPasswordBinding
import com.nafi.airseat.presentation.resetpassword.ResetPasswordActivity
import com.nafi.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpResetPasswordBinding

    private val otpResetPasswordViewModel: OtpResetPasswordViewModel by viewModel()
    private var countDownTimer: CountDownTimer? = null
    private val timerDuration = 60000L // 60 seconds

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
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer =
            object : CountDownTimer(timerDuration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    binding.textResendOTP.text = getString(R.string.text_resend_otp, secondsRemaining)
                }

                override fun onFinish() {
                    binding.textNewCodeOTP.isVisible = true
                    binding.textResendOTP.isVisible = false

                    binding.textInfoOTP.isVisible = true
                }
            }.start()
        binding.textNewCodeOTP.isVisible = false
        binding.textResendOTP.isVisible = true

        binding.textInfoOTP.isVisible = false
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
                    showSnackbar("OTP sent to $email")
                    startTimer()
                },
                doOnError = {
                    binding.textNewCodeOTP.isVisible = true
                    showSnackbar("Failed to send OTP: ${it.exception?.message.orEmpty()}")
                },
                doOnLoading = {
                    binding.textNewCodeOTP.isVisible = false
                },
            )
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar, null)
        customView.findViewById<TextView>(R.id.textView1).text = message
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customView, 0)
        snackbar.show()
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
