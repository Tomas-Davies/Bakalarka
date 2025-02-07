package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingSynthesis

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

data class HearingSynthesisUiState(
    val words: List<String>,
    val initWord: String
)

class HearingSynthesisViewModel(app: LogoApp) : BaseViewModel(app) {
    val repo = app.hearingSynthesisRepository
    val rounds = repo.data
    var currentRound = rounds[roundIdx]
    var currentWords = currentRound.words
    var spellingWord = currentWords.random()
    private var _uiState = MutableStateFlow(HearingSynthesisUiState(currentWords, spellingWord))
    val uiState = _uiState.asStateFlow()

    init {
        count = rounds.size
        viewModelScope.launch { _buttonsEnabled.emit(false) }
    }

    fun validateAnswer(word: String) {
        if (word == spellingWord) {
            playResultSound(result = true)
            viewModelScope.launch {
                _buttonsEnabled.emit(false)
                score++
                showMessage(result = true)
                delay(1500)
                if (nextRound()) updateData()
            }
        } else {
            scoreDesc()
            playResultSound(result = false)
            showMessage(result = false)
        }
    }

    override fun updateData() {
        viewModelScope.launch {
            currentRound = rounds[roundIdx]
            currentWords = currentRound.words
            spellingWord = currentWords.random()
            _uiState.update { state ->
                state.copy(
                    words = currentWords,
                    initWord = spellingWord
                )
            }
        }
    }

    override fun doRestart() {
        updateData()
    }

}

class HearingSynthesisViewModelFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingSynthesisViewModel::class.java)) {
            return HearingSynthesisViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}