package com.c2.airseat.presentation.profilesetting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.c2.airseat.databinding.ActivityProfileSettingBinding
import com.c2.airseat.presentation.deleteaccount.DeleteAccountFragment

class ProfileSettingActivity : AppCompatActivity() {
    private val binding: ActivityProfileSettingBinding by lazy {
        ActivityProfileSettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.ivArrowBack.setOnClickListener {
            onBackPressed()
        }

        binding.itemDeleteAccount.setOnClickListener {
            val dialog = DeleteAccountFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }
}
