package com.swaptech.meet.presentation.utils

import android.util.Base64

fun String.toByteArray(): ByteArray =
    Base64.decode(this, Base64.DEFAULT)

//ddmmyyyy where d - day in month, m - month in year, y - year
fun String.formatToDate(): String {
    val day = this.substring(0..1)
    val month = this.substring(2..3)
    val year = this.substring(4)
    return "$day-$month-$year"
}
