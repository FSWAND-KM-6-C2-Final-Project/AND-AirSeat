package com.c2.airseat.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
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

        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("en", "US"))
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
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("HH.mm")
    val zonedDateTime = ZonedDateTime.parse(this, inputFormatter)
    return zonedDateTime.format(outputFormatter)
}

fun String.toConvertDateFormat(): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = LocalDate.parse(this, inputFormatter)
        val dateTime = date.atStartOfDay()
        dateTime.atOffset(ZoneOffset.UTC).format(outputFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
        "Invalid date format"
    }
}

fun LocalDate.toFormattedCompleteDateNumber(): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return this.format(formatter)
}

fun String.toFormatDateCommon(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val dateTime = LocalDateTime.parse(this, formatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return dateTime.format(outputFormatter)
}
