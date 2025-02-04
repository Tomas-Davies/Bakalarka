package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingMemory

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


data class HearingMemoryUiState(
    val showingImages: List<String> = mutableListOf(),
    val round: Int
)

class HearingMemoryViewModel(private val app: LogoApp) : BaseViewModel(app) {
    private val repo = app.hearingMemoryRepository
    private val rounds = repo.data
    private var currRound = rounds[roundIdx]
    private var allWords = currRound.words
    private var initialWords = getInitialWords()
    private val _uiState = MutableStateFlow(HearingMemoryUiState(
        showingImages = emptyList(),
        round = roundIdx + 1
        ))
    val uiState = _uiState.asStateFlow()
    private var correctAnswerCount = 0

    init {
        count = rounds.size
    }

    fun playInitialWords(onFinish: () -> Unit){
        viewModelScope.launch {
            initialWords.forEach { word ->
                val ctx = app.applicationContext
                val soundId = ctx.resources.getIdentifier(word, "raw", ctx.packageName)
                playSound(soundId)
                delay(2500)
            }
            onFinish()
            showAllWords()
        }
    }

    fun validateAnswer(answer: String): Boolean {
        if (answer in initialWords){
            playResultSound(result = true)
            score++
            correctAnswerCount++
            if (correctAnswerCount == currRound.toBePlayedCount){
                viewModelScope.launch {
                    showMessage(result = true)
                    delay(1500)
                    if (nextRound()) updateData()
                }
            }
            return true
        } else {
            score--
            showMessage(result = false)
            playResultSound(result = false)
            return false
        }
    }

    override fun scorePercentage(): Int {
        var maxScoreCount = 0
        rounds.forEach { round -> maxScoreCount += round.toBePlayedCount }
        return (score * 100) / maxScoreCount
    }

    override fun updateData() {
        correctAnswerCount = 0
        currRound = rounds[roundIdx]
        allWords = currRound.words
        initialWords = getInitialWords()
        _uiState.update { state ->
            state.copy(
                showingImages = emptyList(),
                round = roundIdx + 1
            )
        }
    }

    override fun doRestart() {
        updateData()
    }

    private fun showAllWords() {
        _uiState.update { state ->
            state.copy(
                showingImages = allWords
            )
        }
    }

    private fun getInitialWords(): List<String> {
        val mixed = allWords.shuffled()
        return mixed.subList(0, currRound.toBePlayedCount)
    }

}

class HearingMemoryViewModelFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingMemoryViewModel::class.java)) {
            return HearingMemoryViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}