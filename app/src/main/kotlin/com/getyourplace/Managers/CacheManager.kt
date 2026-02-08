package com.getyourplace.Managers

import android.content.Context
import com.google.gson.Gson
import java.io.File

class CacheManager private constructor() {

    // Changed from private to internal so the inline function can see it
    @PublishedApi
    internal val gson = Gson()

    companion object {
        val shared = CacheManager()
    }

    fun <T> save(context: Context, objectToSave: T, key: String) {
        try {
            val file = File(context.cacheDir, "$key.json")
            val jsonString = gson.toJson(objectToSave)
            file.writeText(jsonString)
            println("Successfully cached data for key: $key")
        } catch (e: Exception) {
            println("Failed to save to cache: ${e.message}")
        }
    }

    /**
     * Reified allows us to keep the Type T at runtime,
     * making Gson's fromJson work just like Swift's JSONDecoder.
     */
    inline fun <reified T> load(context: Context, key: String): T? {
        val file = File(context.cacheDir, "$key.json")
        if (!file.exists()) return null

        return try {
            val jsonString = file.readText()
            // We access the 'internal' gson here
            gson.fromJson(jsonString, T::class.java)
        } catch (e: Exception) {
            println("Failed to load cache for key $key: ${e.message}")
            null
        }
    }

    fun remove(context: Context, key: String) {
        val file = File(context.cacheDir, "$key.json")
        if (file.exists()) {
            file.delete()
        }
    }
}