package com.nafi.airseat.presentation.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.databinding.ActivityPaymentSuccessBinding
import com.nafi.airseat.presentation.order.OrderInfoActivity

class PaymentSuccessActivity : AppCompatActivity() {
    private val binding: ActivityPaymentSuccessBinding by lazy {
        ActivityPaymentSuccessBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mbDetailTicket.setOnClickListener {
            navigateToOrderInfo()
        }
    }

    private fun navigateToOrderInfo() {
        val flightId = intent.getStringExtra("flightId")
        startActivity(
            Intent(this, OrderInfoActivity::class.java).apply {
                putExtra("flightId", flightId)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
