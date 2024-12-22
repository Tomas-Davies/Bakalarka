package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class EyesightMemoryUiState(
    var objectDrawableIds: List<String>,
    var round: Int
)
class EyesightMemoryViewModel(app: LogoApp): BaseViewModel(app) {
    private val memoryRepo = app.eyesightMemoryRepository
    private val data = memoryRepo.data
        .shuffled()
        .sortedBy { item -> item.objects.size }
    private var currentObjects = data[roundIdx].objects
        .map { it.value }
        .toMutableList()
    private var currentExtraObject = ""
    private var _uiState = MutableStateFlow(EyesightMemoryUiState(currentObjects, roundIdx + 1))
    val uiState = _uiState.asStateFlow()
    var enabled = false

    init {
        count = data.size
        chooseExtraObject()
    }

    private fun chooseExtraObject(){
        val randomIndex = Random.nextInt(currentObjects.size)
        currentExtraObject = currentObjects[randomIndex]
        currentObjects.removeAt(randomIndex)
    }

    fun validateAnswer(answer: String): Boolean {
        if (answer == currentExtraObject){
            if (isFirstCorrectAttempt) {
                score++
                isFirstCorrectAttempt = false
            }
            nextRound()
            updateData()
            return true
        } else {
            if (isFirstWrongAttempt){
                score--
                isFirstWrongAttempt = false
            }
            return false
        }
    }

    override fun doRestart() {
        updateData()
    }

    fun showExtraItem(){
        currentObjects.add(currentExtraObject)
        enabled = true
        _uiState.update { state ->
            state.copy(
                objectDrawableIds = currentObjects.shuffled(),
            )
        }
    }

    override fun updateData(){
        enabled = false
        resetAttemptFlags()
        currentObjects = data[roundIdx].objects
            .map { it.value }
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

class EyesightMemoryViewModelFactory(private val app: LogoApp): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightMemoryViewModel::class.java)){
            return EyesightMemoryViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightDifferViewModel")
    }
}