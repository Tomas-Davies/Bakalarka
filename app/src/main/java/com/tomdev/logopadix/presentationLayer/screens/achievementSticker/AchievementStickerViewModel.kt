package com.tomdev.logopadix.presentationLayer.screens.achievementSticker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.dataLayer.repositories.StickerRepository
import com.tomdev.logopadix.dataLayer.repositories.StickerUiModel
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import com.tomdev.logopadix.viewModels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AchievementStickersUiState(
    val stickers: List<StickerUiModel>
)

class AchievementStickerViewModel(
    app: LogoApp,
    private val repo: StickerRepository
): BaseViewModel(app)
{
    private lateinit var _uiState: MutableStateFlow<AchievementStickersUiState>
    lateinit var uiState: StateFlow<AchievementStickersUiState>
        private set

    init {
        viewModelScope.launch {
            val stickers = repo.getStickers()
            Log.w("STICKER DEFS", "count: ${stickers.size}")
            val state = AchievementStickersUiState(stickers)
            _uiState = MutableStateFlow(state)
            uiState = _uiState.asStateFlow()

            _screenState.value = ScreenState.Success
        }
    }
}


class AchievementStickerViewModelFactory(
    private val app: LogoApp,
    private val repo: StickerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AchievementStickerViewModel::class.java)){
            return AchievementStickerViewModel(app, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}