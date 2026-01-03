package com.tomdev.logopadix

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore by preferencesDataStore(name = "user_settings")
val EYESIGHT_SHOW_TIMER_KEY = booleanPreferencesKey("EYESIGHT_SHOW_TIMER")
val WELCOME_POPUP_VERSION_KEY = intPreferencesKey("WELCOME_POPUP_VERSION")
const val CURRENT_WELCOME_POPUP_VERSION: Int = 1