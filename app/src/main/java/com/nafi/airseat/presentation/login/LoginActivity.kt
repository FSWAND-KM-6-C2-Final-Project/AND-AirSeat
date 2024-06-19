package com.nafi.airseat.presentation.login

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
import com.google.android.material.textfield.TextInputLayout
import com.kom.foodapp.utils.highLightWord
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityLoginBinding
import com.nafi.airseat.presentation.main.MainActivity
import com.nafi.airseat.presentation.register.RegisterActivity
import com.nafi.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordActivity
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListener()
    }

    private fun setClickListener() {
        binding.layoutFormLogin.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.layoutFormLogin.tvNavToRegister.highLightWord(getString(R.string.text_sign_up_here)) {
            navigateToRegister()
        }
        binding.layoutFormLogin.tvForgetPassword.setOnClickListener {
            navigateToResetPasswordEmail()
        }
    }

    private fun navigateToRegister() {
        startActivity(
            Intent(this, RegisterActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateToResetPasswordEmail() {
        startActivity(
            Intent(this, ReqChangePasswordActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun doLogin() {
        if (isFormValid()) {
            val email = binding.layoutFormLogin.etEmail.text.toString().trim()
            val password = binding.layoutFormLogin.etPassword.text.toString().trim()
            proceedLogin(email, password)
        }
    }

    private fun proceedLogin(
        email: String,
        password: String,
    ) {
        loginViewModel.doLogin(email, password).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.layoutFormLogin.pbLoading.isVisible = false
                    binding.layoutFormLogin.btnLogin.isVisible = true
                    val token = it.payload.toString()
                    loginViewModel.saveToken(token)
                    navigateToMain()
                },
                doOnError = {
                    binding.layoutFormLogin.pbLoading.isVisible = false
                    binding.layoutFormLogin.btnLogin.isVisible = true
                    showSnackbarError(
                        getString(R.string.text_login_failed),
                    )
                },
                doOnLoading = {
                    binding.layoutFormLogin.pbLoading.isVisible = true
                    binding.layoutFormLogin.btnLogin.isVisible = false
                },
            )
        }
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

    private fun navigateToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun isFormValid(): Boolean {
        val email = binding.layoutFormLogin.etEmail.text.toString().trim()
        val password = binding.layoutFormLogin.etPassword.text.toString().trim()

        return checkEmailValidation(email) && checkPasswordValidation(password, binding.layoutFormLogin.tilPassword)
    }

    private fun checkPasswordValidation(
        confirmPassword: String,
        textInputLayout: TextInputLayout,
    ): Boolean {
        return if (confirmPassword.isEmpty()) {
            binding.layoutFormLogin.tilPassword.isErrorEnabled = true
            binding.layoutFormLogin.tilPassword.error = getString(R.string.error_password_empty)
            false
        } else {
            binding.layoutFormLogin.tilPassword.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutFormLogin.tilEmail.isErrorEnabled = true
            binding.layoutFormLogin.tilEmail.error = getString(R.string.error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormLogin.tilEmail.isErrorEnabled = true
            binding.layoutFormLogin.tilEmail.error = getString(R.string.error_email_invalid)
            false
        } else {
            binding.layoutFormLogin.tilEmail.isErrorEnabled = false
            true
        }
    }

    private fun setupForm() {
        with(binding.layoutFormLogin) {
            tilEmail.isVisible = true
            tilPassword.isVisible = true
        }
    }
}
