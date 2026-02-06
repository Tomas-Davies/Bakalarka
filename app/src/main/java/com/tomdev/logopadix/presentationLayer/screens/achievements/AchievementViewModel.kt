package com.tomdev.logopadix.presentationLayer.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.StreakCalculator
import com.tomdev.logopadix.dataLayer.DayInfo
import com.tomdev.logopadix.viewModels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate


class AchievementViewModel(app: LogoApp): BaseViewModel(app) {
    private val _dailyStreakFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val dailyStreakFlow = _dailyStreakFlow.asStateFlow()

    private val _weekFlow: MutableStateFlow<List<DayInfo>> = MutableStateFlow(emptyList())
    val weekFlow = _weekFlow.asStateFlow()

    val repo = app.dailyActivityRepo

    init {
        viewModelScope.launch {
            val activities = repo.getActivities().associateBy { LocalDate.parse(it.date) }
            val streak = StreakCalculator.calculate(activities, LocalDate.now())
            _dailyStreakFlow.value = streak
            _weekFlow.value = repo.loadCurrentWeek()
        }
    }
}


class AchievementViewModelFactory(
    private val app: LogoApp,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AchievementViewModel::class.java)){
            return AchievementViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}