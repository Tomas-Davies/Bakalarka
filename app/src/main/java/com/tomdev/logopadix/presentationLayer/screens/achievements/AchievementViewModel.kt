package com.tomdev.logopadix.presentationLayer.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tomdev.logopadix.DAY_STREAK_KEY
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.datastore
import com.tomdev.logopadix.viewModels.BaseViewModel
import kotlinx.coroutines.flow.map


class AchievementViewModel(app: LogoApp): BaseViewModel(app) {
    val dayStreakFlow = app.datastore.data.map { pref -> pref[DAY_STREAK_KEY] ?: 0 }

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