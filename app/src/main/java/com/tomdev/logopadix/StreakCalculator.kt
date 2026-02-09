package com.tomdev.logopadix

import com.tomdev.logopadix.dataLayer.DailyActivity
import java.time.LocalDate

class StreakCalculator {
    companion object{
        fun calculate(
            activities: Map<LocalDate, DailyActivity>,
            today: LocalDate
        ): Int {
            var streak = 0
            var date = today

            while (true){
                val entry = activities[date]
                val missingPastDay = date != today && entry == null
                val failedUnfrozenDay = entry?.practiced == false && !entry.frozen
                if (missingPastDay || failedUnfrozenDay) break
                streak++
                date = date.minusDays(1)
            }

            return streak
        }
    }

}