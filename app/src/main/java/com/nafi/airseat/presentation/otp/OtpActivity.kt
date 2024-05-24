package com.nafi.airseat.presentation.otp

import android.accessibilityservice.InputMethod
import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityOtpBinding

class OtpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.otpview.setText("")
        binding.otpview.setOtpCompletionListener {
            hidekeyboard()
            Toast.makeText(this, "Enter Pin: $it", Toast.LENGTH_LONG).show()
        }

        }

    private fun hidekeyboard(){
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE)as InputMethodManager

        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }

    }
}