package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.dataLayer.EyesightMemoryRepo
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EyesightMemoryUiState(
    var objectDrawableIds: List<String>,
    var round: Int
)
class EyesightMemoryViewModel(memoryRepo: EyesightMemoryRepo): ViewModel() {
    private val data = memoryRepo.data
        .shuffled()
        .sortedBy { item -> item.objects.size }

    private var roundIdx = 0
    private var currentObjects = data[roundIdx].objects
        .map { it.value }
        .toMutableList()

    private var currentExtraObject = data[roundIdx].extraObject.value
    private var _uiState = MutableStateFlow(EyesightMemoryUiState(currentObjects, roundIdx + 1))
    val uiState = _uiState.asStateFlow()
    private var _screenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    var screenState = _screenState.asStateFlow()
    private var isFirstCorrectAttempt = true
    private var isFirstWrongAttempt = true
    private var score = 0
    var count = data.size
    var enabled = false

    fun validateAnswer(answer: String): Boolean {
        if (answer == currentExtraObject){
            if (isFirstCorrectAttempt) {
                score++
                isFirstCorrectAttempt = false
            }
            indexInc()
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

    fun restart(){
        roundIdx = 0
        score = 0
        updateData()
        _screenState.value = ScreenState.Running
    }

    fun scorePercentage(): Int {
        val correctCount = score
        val questionCount = count
        return (correctCount * 100) / questionCount
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

    private fun updateData(){
        enabled = false
        resetAttemptFlags()
        currentObjects = data[roundIdx].objects
            .map { it.value }
            .toMutableList()

        currentExtraObject = data[roundIdx].extraObject.value
        _uiState.update { state ->
            state.copy(
                objectDrawableIds = currentObjects,
                round = roundIdx + 1
            )
        }
    }

    private fun indexInc(){
        if (roundIdx+1 < data.size){
            roundIdx++
        } else {
            _screenState.value = ScreenState.Finished
        }
    }

    private fun resetAttemptFlags(){
        isFirstCorrectAttempt = true
        isFirstWrongAttempt = true
    }
}

class EyesightMemoryViewModelFactory(private val memoryRepo: EyesightMemoryRepo): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightMemoryViewModel::class.java)){
            return EyesightMemoryViewModel(memoryRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightDifferViewModel")
    }
}