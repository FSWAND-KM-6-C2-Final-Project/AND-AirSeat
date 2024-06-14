package com.nafi.airseat.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateConvert {
    fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return date.format(formatter)
    }
}
