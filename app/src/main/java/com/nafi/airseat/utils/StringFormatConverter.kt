package com.nafi.airseat.utils

fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) return this // Return the original string if it's empty

    return substring(0, 1).toUpperCase() + substring(1)
}
