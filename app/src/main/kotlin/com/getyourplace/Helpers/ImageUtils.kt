package com.getyourplace.Helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Converts a Resource Name (String) to Base64
 * Equivalent to your `asAssetBase64` extension
 */
fun String.asAssetBase64(context: Context): String {
    val resId = context.resources.getIdentifier(this, "drawable", context.packageName)
    if (resId == 0) return ""

    val bitmap = BitmapFactory.decodeResource(context.resources, resId)
    return bitmap.toBase64()
}

/**
 * Extension to convert Bitmap to Base64 String
 */
fun Bitmap.toBase64(compressionQuality: Int = 80): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, compressionQuality, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

/**
 * Extension to convert Base64 String back to Bitmap
 * Equivalent to `toSwiftUIImage`
 */
fun String.toBitmap(): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}