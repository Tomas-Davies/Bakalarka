package com.tomdev.logopadix.viewModels

import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.services.DayStreakService

abstract class DifficultyRoundsViewModel(
    protected val difficulty: String,
    app: LogoApp,
    streakService: DayStreakService
): RoundsViewModel(app, streakService) {

}