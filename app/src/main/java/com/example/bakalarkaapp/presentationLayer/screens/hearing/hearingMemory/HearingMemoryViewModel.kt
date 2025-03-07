package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingMemory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.WordContent
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HearingMemoryUiState(
    val showingObjects: List<WordContent> = mutableListOf(),
    val round: Int
)

class HearingMemoryViewModel(app: LogoApp) : RoundsViewModel(app) {
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

    override var roundSetSize = 1

    fun playInitallObjects(onFinish: () -> Unit) {
        viewModelScope.launch {
            initallObjects.forEach { obj ->
                val soundName = obj.soundName ?: ""
                playSoundByName(soundName)
                delay(2000)
            }
            onFinish()
            showAllObjects()
        }
    }

    suspend fun onCardClick(answer: String): Boolean {
        clickedCounterInc()
        if (answer in initiallObjImgNames) {
            onCorrectAnswer()
            return true
        }
        onWrongAnswer()
        return false
    }

    private fun onCorrectAnswer(){
        playResultSound(result = true)
        scoreInc()
        correctAnswerCount++
        if (correctAnswerCount == currRound.toBePlayedCount){
            roundsCompletedInc()
            if (roundSetCompletedCheck()) {
                showRoundSetDialog()
            } else {
                doContinue()
            }
        }
    }


    private suspend fun onWrongAnswer(){
        playResultSound(result = false)
        showWrongMessage()
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


    private fun showAllObjects() {
        _uiState.update { state ->
            state.copy(
                showingObjects = allObjects
            )
        }
    }

    private fun getInitallObjects(): List<WordContent> {
        val mixed = allObjects.shuffled()
        return mixed.subList(0, currRound.toBePlayedCount)
    }

    private fun getInitiallObJImages(): List<String> {
        return initallObjects.map { obj -> obj.imageName ?: "" }
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