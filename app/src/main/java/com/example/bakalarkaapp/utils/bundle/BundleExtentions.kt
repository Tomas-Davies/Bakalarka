package com.example.bakalarkaapp.utils.bundle

import android.content.Intent
import android.os.Build
import java.io.Serializable

fun <T : Serializable> Intent.serializable(key: String, clazz: Class<T>): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, clazz)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}