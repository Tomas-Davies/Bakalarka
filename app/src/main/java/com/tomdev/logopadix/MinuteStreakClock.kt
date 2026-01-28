package com.tomdev.logopadix

class MinuteStreakClock: StreakClock {
    override fun now(): Long {
        return java.time.Instant.now().epochSecond / 60
    }
}