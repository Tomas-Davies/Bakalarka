package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HearingFonematicUiState(
    val words: List<String>,
    val playedWord: String
)

class HearingFonematicViewModel(app: LogoApp) : BaseViewModel(app) {
    private val repo = app.hearingFonematicRepository
    private val rounds = repo.data.shuffled()
    private var currentRound = rounds[roundIdx]
    private var currentWords = currentRound.words
    private val _uiState = MutableStateFlow(
        HearingFonematicUiState(
            words = currentWords,
            playedWord = currentWords.random()
        )
    )

    val uiState = _uiState.asStateFlow()

    init {
        count = rounds.count()
    }

    fun validateAnswer(answer: String): Boolean {
        if (answer == _uiState.value.playedWord) {
            score++
            viewModelScope.launch {
                showMessage(result = true)
                delay(1500)
                if (nextRound()) {
                    updateData()
                }
            }
            return true
        } else {
            showMessage(result = false)
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
                words = currentWords,
                playedWord = currentWords.random()
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