package com.nafi.airseat.presentation.common.sharedactivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.presentation.common.sharedviewmodel.SharedViewModel

open class SharedActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
}
