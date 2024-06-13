package com.nafi.airseat.utils

import java.text.NumberFormat
import java.util.Locale

fun Long.toCurrencyFormat(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Use Indonesian locale for IDR
    return formatter.format(this).replace("Rp", "IDR ") // Replace Rp with IDR for consistency
}
