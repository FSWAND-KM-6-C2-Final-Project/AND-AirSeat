package com.c2.airseat.presentation.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.c2.airseat.databinding.ActivityOrderInfoBinding

class OrderInfoActivity : AppCompatActivity() {
    private val binding: ActivityOrderInfoBinding by lazy {
        ActivityOrderInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
