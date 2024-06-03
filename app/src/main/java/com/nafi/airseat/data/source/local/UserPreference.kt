package com.nafi.airseat.data.source.local

import android.content.Context
import com.nafi.airseat.utils.SharedPreferenceUtils

interface UserPreference {
    fun isUsingGridMode(): Boolean

    fun setUsingGridMode(isUsingGridMode: Boolean)
}



