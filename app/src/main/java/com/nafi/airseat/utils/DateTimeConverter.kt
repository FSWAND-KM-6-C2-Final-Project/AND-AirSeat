package com.nafi.airseat.utils

import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

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

fun String?.toMonthYearFormat(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(this ?: "") ?: return "Date Format Wrong"

        val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        return "Date Format Wrong"
    }
}

fun String?.toCompleteDateFormat(): String? {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(this ?: "") ?: return null

        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("en", "US")) // "id" for Indonesian locale
        outputFormat.format(date)
    } catch (e: Exception) {
        null
    }
}

fun String?.toTimeFormat(): String? {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(this ?: "") ?: return null

        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        null
    }
}

fun String.toTimeClock(): String {
    // Define the input date-time formatter
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    // Define the output time formatter
    val outputFormatter = DateTimeFormatter.ofPattern("HH.mm")

    // Parse the input date-time string
    val zonedDateTime = ZonedDateTime.parse(this, inputFormatter)

    // Format the time part and return it
    return zonedDateTime.format(outputFormatter)
}
