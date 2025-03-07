package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class EyesightMemoryUiState(
    var objectDrawableIds: List<String>,
    var round: Int
)

class EyesightMemoryViewModel(app: LogoApp) : RoundsViewModel(app) {
    private val memoryRepo = app.eyesightMemoryRepository
    private val data = memoryRepo.data
        .shuffled()
        .sortedBy { item -> item.objects.size }
    private var currentObjects = data[roundIdx].objects
        .map { obj -> obj.text ?: "" }
        .toMutableList()
    private var currentExtraObject = ""
    private var _uiState = MutableStateFlow(EyesightMemoryUiState(currentObjects, roundIdx + 1))
    val uiState = _uiState.asStateFlow()
    override var roundSetSize = 3
    init {
        count = data.size
        chooseExtraObject()
        disableButtons()
    }

    suspend fun onCardClick(name: String): Boolean {
        clickedCounterInc()
        if (name == currentExtraObject){
            onCorrectAnswer()
            return true
        } else {
            onWrongAnswer()
            return false
        }
    }

    private suspend fun onCorrectAnswer(){
        disableButtons()
        playOnCorrectSound()
        showCorrectMessage()
        scoreInc()
        roundsCompletedInc()

        if (roundSetCompletedCheck()){
            showRoundSetDialog()
        } else {
            doContinue()
        }
    }

    private suspend fun onWrongAnswer(){
        playOnWrongSound()
        showWrongMessage()
    }

    override fun doContinue() {
        super.doContinue()
        disableButtons()
    }

    private fun chooseExtraObject() {
        val randomIndex = Random.nextInt(currentObjects.size)
        currentExtraObject = currentObjects[randomIndex]
        currentObjects.removeAt(randomIndex)
    }


    fun showExtraItem() {
        currentObjects.add(currentExtraObject)
        enableButtons()
        _uiState.update { state ->
            state.copy(
                objectDrawableIds = currentObjects.shuffled(),
            )
        }
    }

    override fun updateData() {
        currentObjects = data[roundIdx].objects
            .map { obj -> obj.text ?: "" }
            .toMutableList()
        chooseExtraObject()
        _uiState.update { state ->
            state.copy(
                objectDrawableIds = currentObjects,
                round = roundIdx + 1
            )
        }
    }
}

class EyesightMemoryViewModelFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightMemoryViewModel::class.java)) {
            return EyesightMemoryViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}