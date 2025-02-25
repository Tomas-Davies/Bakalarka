package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingMemory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.ValidatableViewModel
import com.example.bakalarkaapp.dataLayer.models.RoundContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HearingMemoryUiState(
    val showingObjects: List<RoundContent> = mutableListOf(),
    val round: Int
)

class HearingMemoryViewModel(app: LogoApp) : ValidatableViewModel(app) {
    private val repo = app.hearingMemoryRepository
    private val rounds = repo.data
    private var currRound = rounds[roundIdx]
    private var allObjects = currRound.objects
    private var initallObjects = getInitallObjects()
    private var initiallObjImgNames = getInitiallObJImages()
    private val _uiState = MutableStateFlow(
        HearingMemoryUiState(
            showingObjects = emptyList(),
            round = roundIdx + 1
        )
    )
    val uiState = _uiState.asStateFlow()
    private var correctAnswerCount = 0

    init {
        count = rounds.size
    }

    override fun validationCond(answer: IValidationAnswer): Boolean {
       return correctAnswerCount == currRound.toBePlayedCount
    }

    override fun playOnCorrectSound() {}
    override fun playOnWrongSound() {}
    override fun scoreInc() {}
    override fun scoreDesc() {}
    override fun messageShowWrong() {}

    fun playInitallObjects(onFinish: () -> Unit) {
        viewModelScope.launch {
            initallObjects.forEach { obj ->
                val soundName = obj.soundName ?: ""
                playSoundByName(soundName)
                delay(2000)
            }
            onFinish()
            showallObjects()
        }
    }

    fun onCardClick(answer: String): Boolean {
        if (answer in initiallObjImgNames) {
            playResultSound(result = true)
            score++
            correctAnswerCount++
            validateAnswer(IValidationAnswer.BlankAnswer)
            return true
        }
        playResultSound(result = false)
        showMessage(result = false)
        if (score > 0) score--
        return false
    }

    override fun scorePercentage(): Int {
        var maxScoreCount = 0
        rounds.forEach { round -> maxScoreCount += round.toBePlayedCount }
        return (score * 100) / maxScoreCount
    }

    override fun updateData() {
        correctAnswerCount = 0
        currRound = rounds[roundIdx]
        allObjects = currRound.objects
        initallObjects = getInitallObjects()
        initiallObjImgNames = getInitiallObJImages()
        _uiState.update { state ->
            state.copy(
                showingObjects = emptyList(),
                round = roundIdx + 1
            )
        }
    }

    override fun doRestart() {
        updateData()
    }

    private fun showallObjects() {
        _uiState.update { state ->
            state.copy(
                showingObjects = allObjects
            )
        }
    }

    private fun getInitallObjects(): List<RoundContent> {
        val mixed = allObjects.shuffled()
        return mixed.subList(0, currRound.toBePlayedCount)
    }

    private fun getInitiallObJImages(): List<String> {
        return initallObjects.map { obj -> obj.imgName ?: "" }
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