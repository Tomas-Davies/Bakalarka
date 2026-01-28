package com.tomdev.logopadix.presentationLayer.screens.achievementCollectionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.DAY_STREAK_KEY
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.dataLayer.repositories.Achievement
import com.tomdev.logopadix.dataLayer.repositories.AchievementRepo
import com.tomdev.logopadix.datastore
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import com.tomdev.logopadix.viewModels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class AchievementUiState(
    val achievements: List<Achievement> = emptyList()
)

class AchievementCollectionViewModel(app: LogoApp, repo: AchievementRepo): BaseViewModel(app) {
    private lateinit var _achievementsUiState: MutableStateFlow<AchievementUiState>
    lateinit var achievementsUiState: StateFlow<AchievementUiState>

    val dayStreakFlow = app.datastore.data.map { prefs -> prefs[DAY_STREAK_KEY] ?: 0 }

    init {
        viewModelScope.launch {
            val loadedData = repo.loadData()
            val uiState = AchievementUiState(loadedData)
            _achievementsUiState = MutableStateFlow(uiState)
            achievementsUiState = _achievementsUiState.asStateFlow()
            _screenState.value = ScreenState.Success
        }
    }
}

class AchievementCollectionViewModelFactory(
    private val app: LogoApp,
    private val repo: AchievementRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AchievementCollectionViewModel::class.java)){
            return AchievementCollectionViewModel(app, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}