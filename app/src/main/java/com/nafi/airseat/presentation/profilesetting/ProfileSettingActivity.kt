package com.nafi.airseat.presentation.profilesetting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.databinding.ActivityProfileSettingBinding
import com.nafi.airseat.presentation.deleteaccount.DeleteAccountFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileSettingActivity : AppCompatActivity() {
    private val binding: ActivityProfileSettingBinding by lazy {
        ActivityProfileSettingBinding.inflate(layoutInflater)
    }
    private val viewModel: ProfileSettingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.itemDeleteAccount.setOnClickListener {
            val dialog = DeleteAccountFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }
}
