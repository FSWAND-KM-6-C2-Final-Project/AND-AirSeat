package com.nafi.airseat.presentation.otpaccount

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityOtpBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.hideKeyboard
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.showSnackBarError
import com.nafi.airseat.utils.showSnackBarSuccess
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val otpViewModel: OtpViewModel by viewModel()
    private var countDownTimer: CountDownTimer? = null
    private val timerDuration = 60000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()
        setTextData()
        startTimer()
    }

    private fun setTextData() {
        val email = intent.getStringExtra("email")
        binding.textEmail.text = email
        binding.otpview.setText("")
        binding.otpview.setOtpCompletionListener { otp ->
            hideKeyboard()
            if (email != null) {
                verifyOtp(email, otp)
            }
        }
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer =
            object : CountDownTimer(timerDuration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    binding.textResendOTP.text =
                        getString(R.string.text_resend_otp, secondsRemaining)
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

    private fun setClickListeners() {
        binding.textNewCodeOTP.setOnClickListener {
            val email = binding.textEmail.text.toString()
            resendOtp(email)
        }
    }

    private fun resendOtp(email: String) {
        otpViewModel.doVerifyResendOtp(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.textNewCodeOTP.isVisible = true
                    showSnackBarSuccess("$it")
                    startTimer()
                },
                doOnError = {
                    binding.textNewCodeOTP.isVisible = true
                    if (it.exception is ApiErrorException) {
                        showSnackBarError("$it")
                    } else if (it.exception is NoInternetException) {
                        showSnackBarError(getString(R.string.text_no_internet))
                    }
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
        otpViewModel.doVerify(email, code).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    showSnackBarSuccess("$it")
                    navigateToLogin()
                },
                doOnError = {
                    if (it.exception is ApiErrorException) {
                        showSnackBarError("${it.exception.errorResponse.message}")
                    } else if (it.exception is NoInternetException) {
                        showSnackBarError(getString(R.string.text_no_internet))
                    }
                },
                doOnLoading = {
                },
            )
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
