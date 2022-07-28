package com.swaptech.meet.presentation.utils

import android.util.Base64

fun String.toByteArray(): ByteArray =
    Base64.decode(this, Base64.DEFAULT)
