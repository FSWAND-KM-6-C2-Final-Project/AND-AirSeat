package com.c2.airseat.presentation.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.c2.airseat.databinding.ActivityPaymentSuccessBinding
import com.c2.airseat.presentation.main.MainActivity

class PaymentSuccessActivity : AppCompatActivity() {
    private val binding: ActivityPaymentSuccessBinding by lazy {
        ActivityPaymentSuccessBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mbDetailTicket.setOnClickListener {
            navigateToDetailTicket()
        }
    }

    private fun navigateToDetailTicket() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
