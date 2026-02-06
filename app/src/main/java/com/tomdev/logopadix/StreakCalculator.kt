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
                if (entry?.practiced == true || entry?.frozen == true){
                    streak++
                    date = date.minusDays(1)
                } else break
            }

            return streak
        }
    }

}