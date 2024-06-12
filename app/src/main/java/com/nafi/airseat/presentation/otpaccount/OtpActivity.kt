package com.nafi.airseat.presentation.otpaccount

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityOtpBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.register.RegisterActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    private val otpViewModel: OtpViewModel by viewModel()
    private var countDownTimer: CountDownTimer? = null
    private val timerDuration = 60000L // 60 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()

        val email = intent.getStringExtra("email")
        binding.textEmail.text = email

        binding.otpview.setText("")
        binding.otpview.setOtpCompletionListener { otp ->
            hidekeyboard()
            if (email != null) {
                verifyOtp(email, otp)
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
            navigateToRegister()
        }
        binding.textNewCodeOTP.setOnClickListener {
            val email = binding.textEmail.text.toString()
            resendOtp(email)
        }
    }

    private fun navigateToRegister() {
        startActivity(
            Intent(this, RegisterActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun resendOtp(email: String) {
        otpViewModel.doVerifResendOtp(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.textNewCodeOTP.isVisible = true
                    Toast.makeText(
                        this,
                        "OTP sent to $email",
                        Toast.LENGTH_SHORT,
                    ).show()
                    startTimer()
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

    private fun verifyOtp(
        email: String,
        code: String,
    ) {
        otpViewModel.doVerif(email, code).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(
                        this,
                        "OTP verification successful",
                        Toast.LENGTH_SHORT,
                    ).show()
                    navigateToLogin() // Replace with actual navigation
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
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply { // Replace with actual target activity
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
