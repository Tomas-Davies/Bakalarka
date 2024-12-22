package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HearingFonematicUiState(
    val imageResource1: String,
    val imageResource2: String,
    val imageResource3: String,
    val soundResource: String
)

class HearingFonematicViewModel(app: LogoApp) : BaseViewModel(app) {
    private val repo = app.hearingFonematicRepository
    private val rounds = repo.data.shuffled()
    private var currentRound = rounds[roundIdx]
    private var currentWords = currentRound.words
    private val _uiState = MutableStateFlow(
        HearingFonematicUiState(
            currentWords[0].value,
            currentWords[1].value,
            currentWords[2].value,
            currentWords.random().value
        )
    )

    val uiState = _uiState.asStateFlow()

    init {
        count = rounds.count()
    }

    fun validateAnswer(answer: String): Boolean {
        if (answer == _uiState.value.soundResource) {
            score++
            if (nextRound()) {
                updateData()
            }
            return true
        } else {
            if (isFirstWrongAttempt) {
                score--
                isFirstWrongAttempt = false
            }
            return false
        }
    }

    override fun updateData() {
        currentRound = rounds[roundIdx]
        currentWords = currentRound.words
        isFirstWrongAttempt = true

        _uiState.update { state ->
            state.copy(
                imageResource1 = currentWords[0].value,
                imageResource2 = currentWords[1].value,
                imageResource3 = currentWords[2].value,
                soundResource = currentWords.random().value
            )
        }
    }

    override fun doRestart() {
        updateData()
    }
}

class HearingFonematicFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingFonematicViewModel::class.java)) {
            return HearingFonematicViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}