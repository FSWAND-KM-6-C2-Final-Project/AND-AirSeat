package com.nafi.airseat.utils

import java.util.Locale

fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) return this

    return substring(0, 1).uppercase(Locale.ROOT) + substring(1)
}

fun String.toSeatClassNameMultiLine(): String {
    return if (this.length == 11) {
        this.split('_')
            .joinToString(separator = " ") { word ->
                word.replaceFirstChar { it.uppercaseChar() }
            }
    } else {
        this.split('_')
            .joinToString(separator = "\r\n") { word ->
                word.replaceFirstChar { it.uppercaseChar() }
            }
    }
}
