package com.tomdev.logopadix.viewModels

import com.tomdev.logopadix.LogoApp

abstract class DifficultyRoundsViewModel(
    protected val difficulty: String,
    app: LogoApp
): RoundsViewModel(app) {

}