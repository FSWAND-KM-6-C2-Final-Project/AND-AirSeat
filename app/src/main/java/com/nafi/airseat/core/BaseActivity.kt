package com.nafi.airseat.core

import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseActivity : AppCompatActivity() {
    private val baseViewModel: BaseViewModel by viewModel()

    fun cleatToken() {
        baseViewModel.clearSession()
    }
}
