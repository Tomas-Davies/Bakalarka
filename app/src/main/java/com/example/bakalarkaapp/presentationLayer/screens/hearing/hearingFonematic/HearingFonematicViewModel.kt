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
    private val rounds = repo.data.shuffled().sortedBy { round -> round.words.size }
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
        viewModelScope.launch { _buttonsEnabled.emit(false) }
    }

    fun validateAnswer(answer: String) {
        if (answer == _uiState.value.playedWord) {
            playResultSound(result = true)
            viewModelScope.launch {
                _buttonsEnabled.emit(false)
                score++
                showMessage(result = true)
                delay(1500)
                if (nextRound()) updateData()
            }
        } else {
            playResultSound(result = false)
            showMessage(result = false)
            scoreDesc()
        }
    }

    override fun updateData() {
        currentRound = rounds[roundIdx]
        currentWords = currentRound.words

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