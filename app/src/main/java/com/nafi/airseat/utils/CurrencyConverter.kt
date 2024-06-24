package com.nafi.airseat.utils

import java.text.NumberFormat
import java.util.Locale

fun Long.toCurrencyFormat(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(this).replace("Rp", "IDR ")
}
