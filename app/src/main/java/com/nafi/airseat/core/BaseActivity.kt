package com.nafi.airseat.core

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseActivity : AppCompatActivity() {
    private val baseViewModel: BaseViewModel by viewModel()

    fun clearToken() {
        baseViewModel.clearSession()
    }

    val token: LiveData<String> get() = baseViewModel.token
}
