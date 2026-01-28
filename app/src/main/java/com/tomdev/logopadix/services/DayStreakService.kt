package com.tomdev.logopadix.services

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.tomdev.logopadix.DAY_STREAK_DATE_KEY
import com.tomdev.logopadix.DAY_STREAK_KEY
import com.tomdev.logopadix.DayStreakClock
import com.tomdev.logopadix.StreakClock
import com.tomdev.logopadix.datastore
import kotlinx.coroutines.flow.first

class DayStreakService(
    appCtx: Context,
    private val clock: StreakClock = DayStreakClock()//MinuteStreakClock()
) {
    private val ds = appCtx.datastore

    suspend fun checkStreak(){
        val prefs = ds.data.first()
        val oldDate = prefs[DAY_STREAK_DATE_KEY] ?: 0L
        val streakCount = prefs[DAY_STREAK_KEY] ?: 0
        val currDate = clock.now()
        val diff = currDate - oldDate
        if (diff == 0L) return

        when {
            oldDate == 0L || diff > 1 -> {
                ds.edit { prefs ->
                    prefs[DAY_STREAK_DATE_KEY] = currDate
                    prefs[DAY_STREAK_KEY] = 1
                }
            }
            diff == 1L -> {
                ds.edit { prefs ->
                    prefs[DAY_STREAK_DATE_KEY] = currDate
                    prefs[DAY_STREAK_KEY] = streakCount + 1
                }
            }
        }
    }
}