package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingMemory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.HearingMemoryRound
import com.example.bakalarkaapp.dataLayer.models.WordContent
import com.example.bakalarkaapp.dataLayer.repositories.HearingMemoryRepo
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

class HearingMemoryViewModel(
    private val repo: HearingMemoryRepo,
    app: LogoApp
) : RoundsViewModel(app)
{
    private lateinit var rounds: List<HearingMemoryRound>
    private lateinit var currRound: HearingMemoryRound
    private lateinit var allObjects: List<WordContent>
    private lateinit var initallObjects: List<WordContent>
    private lateinit var initiallObjImgNames: List<String>
    private val _uiState = MutableStateFlow(
        HearingMemoryUiState(
            showingObjects = emptyList(),
            round = roundIdx + 1
        )
    )
    val uiState = _uiState.asStateFlow()
    private var correctAnswerCount = 0
    override var roundSetSize = 1

    init {
        viewModelScope.launch {
            repo.loadData()
            rounds = repo.data
            currRound = rounds[roundIdx]
            allObjects = currRound.objects
            initallObjects = getInitallObjects()
            initiallObjImgNames = getInitiallObJImages()
            count = rounds.size
            dataLoaded()
        }
    }


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


class HearingMemoryViewModelFactory(
    private val repo: HearingMemoryRepo,
    private val app: LogoApp
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingMemoryViewModel::class.java)) {
            return HearingMemoryViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}