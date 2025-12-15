package com.tomdev.logopadix

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore by preferencesDataStore(name = "user_settings")
val EYESIGHT_SHOW_TIMER_KEY = booleanPreferencesKey("EYESIGHT_SHOW_TIMER")