package com.tomdev.logopadix

class DayStreakClock: StreakClock {
    override fun now(): Long {
        return java.time.LocalDate.now().toEpochDay()
    }
}