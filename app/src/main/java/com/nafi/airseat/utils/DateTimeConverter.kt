package com.nafi.airseat.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String?.toFormattedDate(
    pattern: String,
    language: String,
    country: String,
): String? {
    return try {
        val locale = Locale(language, country)
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern(pattern, locale)

        val parsedDate = ZonedDateTime.parse(this, inputFormatter)
        parsedDate.format(outputFormatter)
    } catch (e: Exception) {
        null
    }
}

fun String?.toFormattedDateNotification() = this.toFormattedDate("d MMMM, HH.mm", "en", "US")
