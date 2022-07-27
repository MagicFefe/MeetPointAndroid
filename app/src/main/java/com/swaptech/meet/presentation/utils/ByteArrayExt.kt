package com.swaptech.meet.presentation.utils

import android.util.Base64

fun ByteArray.toBase64(): ByteArray =
    Base64.encode(this, Base64.DEFAULT)

