package com.example.noteapplication.Utilities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object Converters {
    fun stringToBitmap(string: String): Bitmap? {
        val byteArray = android.util.Base64.decode(string, android.util.Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun bitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }
}