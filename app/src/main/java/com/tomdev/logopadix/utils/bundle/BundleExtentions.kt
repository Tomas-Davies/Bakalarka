package com.tomdev.logopadix.utils.bundle

import android.content.Intent
import android.os.Build
import java.io.Serializable

/**
 * Extension function to safely retrieve a Serializable extra from an Intent.
 *
 * This function provides compatibility across different Android APIs for retrieving
 * Serializable objects from Intents:
 * - For API 33+: Uses the type-safe getSerializableExtra(String, Class).
 * - For older versions: Uses the legacy getSerializableExtra(String).
 *
 * @param key The name of the extra to retrieve.
 * @param clazz The Class object of the Serializable type T (used for type-safety on API 33+).
 * @return The Serializable object of type T, or null if not found.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Intent.getSdkBasedSerializableExtra(key: String, clazz: Class<T>): T? {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> return getSerializableExtra(key, clazz)
        else -> {
            @Suppress("DEPRECATION")
            return getSerializableExtra(key) as? T
        }
    }
}