package com.nafi.airseat.presentation.flightdetail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.databinding.ActivityFlightDetailBinding
import com.nafi.airseat.presentation.payment.WebViewMidtransActivity

class FlightDetailActivity : AppCompatActivity() {
    private val binding: ActivityFlightDetailBinding by lazy {
        ActivityFlightDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.cvSectionCheckout.btnPayment.setOnClickListener {
            openUrlFromWebView()
        }
    }

    private fun openUrlFromWebView() {
        val intent = Intent(this, WebViewMidtransActivity::class.java)
        intent.putExtra("URL", "https://sample-demo-dot-midtrans-support-tools.et.r.appspot.com/snap-redirect/")
        startActivity(intent)
    }
}
