package com.swaptech.meet.presentation.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.resizeToProfileImage(): Bitmap {
    return Bitmap
        .createScaledBitmap(this, 200, 200, false)
}

fun Bitmap.toByteArray(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}
