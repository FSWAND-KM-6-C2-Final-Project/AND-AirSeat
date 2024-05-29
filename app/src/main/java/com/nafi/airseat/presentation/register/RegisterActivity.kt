package com.nafi.airseat.presentation.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.kom.foodapp.utils.highLightWord
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityRegisterBinding
import com.nafi.airseat.presentation.login.LoginActivity
import com.nafi.airseat.presentation.otp.OtpActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnRegister.setOnClickListener {
            doRegister()
        }
        binding.tvNavToLogin.highLightWord(getString(R.string.text_login_here)) {
            navigateToLogin()
        }
        binding.icDetailBackButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToOtp(email: String) {
        startActivity(
            Intent(this, OtpActivity::class.java).apply {
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

    private fun doRegister() {
        if (isFormValid()) {
            val email = binding.layoutForm.etEmail.text.toString().trim()
            val password = binding.layoutForm.etPassword.text.toString().trim()
            val phonenumber = binding.layoutForm.etPhone.text.toString().trim()
            val confirmpassword = binding.layoutForm.etConfirmPassword.text.toString().trim()
            val fullName = binding.layoutForm.etName.text.toString().trim()
            proceedRegister(fullName, email, phonenumber, confirmpassword, password)
        }
    }

    private fun proceedRegister(
        fullName: String,
        email: String,
        phonenumber: String,
        confirmpassword: String,
        password: String,
    ) {
        registerViewModel.doRegister(fullName, email, phonenumber, confirmpassword, password).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    sendOtp(email)
                    Toast.makeText(
                        this,
                        getString(R.string.text_register_success),
                        Toast.LENGTH_SHORT,
                    ).show()
                    navigateToOtp(email)
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    Toast.makeText(
                        this,
                        "Register Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnRegister.isVisible = false
                },
            )
        }
    }

    private fun setupForm() {
        with(binding.layoutForm) {
            tilEmail.isVisible = true
            tilPassword.isVisible = true
            tilPhone.isVisible = true
            tilName.isVisible = true
            tilConfirmPassword.isVisible = true
        }
    }

    private fun isFormValid(): Boolean {
        val password = binding.layoutForm.etPassword.text.toString().trim()
        val confirmPassword = binding.layoutForm.etConfirmPassword.text.toString().trim()
        val fullName = binding.layoutForm.etName.text.toString().trim()
        val email = binding.layoutForm.etEmail.text.toString().trim()
        val phonenumber = binding.layoutForm.etPhone.text.toString().trim()

        return checkNameValidation(fullName) && checkEmailValidation(email) &&
            checkPasswordValidation(password, binding.layoutForm.tilPassword) &&
            checkPasswordValidation(confirmPassword, binding.layoutForm.tilConfirmPassword) &&
            checkPwdAndConfirmPwd(password, confirmPassword) &&
            checkphoneValidation(phonenumber)
    }

    private fun checkNameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun checkphoneValidation(phonenumber: String): Boolean {
        return if (phonenumber.isEmpty()) {
            binding.layoutForm.tilPhone.isErrorEnabled = true
            binding.layoutForm.tilPhone.error = getString(R.string.text_error_phone_cannot_empty)
            false
        } else {
            binding.layoutForm.tilPhone.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutForm.tilEmail.isErrorEnabled = true
            binding.layoutForm.tilEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutForm.tilEmail.isErrorEnabled = true
            binding.layoutForm.tilEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutForm.tilEmail.isErrorEnabled = false
            true
        }
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
            binding.layoutForm.tilPassword.isErrorEnabled = true
            binding.layoutForm.tilPassword.error =
                getString(R.string.text_password_does_not_match)
            binding.layoutForm.tilConfirmPassword.isErrorEnabled = true
            binding.layoutForm.tilConfirmPassword.error =
                getString(R.string.text_password_does_not_match)
            false
        } else {
            binding.layoutForm.tilPassword.isErrorEnabled = false
            binding.layoutForm.tilConfirmPassword.isErrorEnabled = false
            true
        }
    }

    private fun sendOtp(email: String) {
        registerViewModel.doVerifResendOtp(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    Toast.makeText(
                        this,
                        "OTP sent to $email",
                        Toast.LENGTH_SHORT,
                    ).show()
                    // ini navigate to otp
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    Toast.makeText(
                        this,
                        "Failed to send OTP: ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnRegister.isVisible = false
                },
            )
        }
    }
}