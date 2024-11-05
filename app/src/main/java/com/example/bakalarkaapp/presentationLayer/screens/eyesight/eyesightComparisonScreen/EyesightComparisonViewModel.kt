package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.dataLayer.EyesightComparisonRepo
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EyesightComparisonUiState (
    val imageId: String,
    val answer: Boolean,
    val restartTrigger: Int = 0
)

class EyesightComparisonViewModel(comparisonDataRepository: EyesightComparisonRepo): ViewModel() {
    private var data = comparisonDataRepository.data.shuffled()
    private var idx = 0
    private var currentItem = data[0]
    private val _uiState = MutableStateFlow(EyesightComparisonUiState(currentItem.imageId.value, currentItem.isSameShape.value.toBoolean()))
    val uiState: StateFlow<EyesightComparisonUiState> = _uiState.asStateFlow()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()
    private var score: Int = 0
    private var btnOneClickedFlag: Boolean = false
    private var btnTwoClickedFlag: Boolean = false


    fun scoreInc(){
        if (!btnOneClickedFlag) {
            score++
            btnOneClickedFlag = true
        }
    }
    fun scoreDesc(){
        if (!btnTwoClickedFlag) {
            score--
            btnTwoClickedFlag = true
        }
    }

    fun updateData(){
        indexInc()
        currentItem = data[idx]
        _uiState.update { currentState ->
            currentState.copy(
                imageId = currentItem.imageId.value,
                answer = currentItem.isSameShape.value.toBoolean(),
                restartTrigger = idx
            )
        }
        btnOneClickedFlag = false
        btnTwoClickedFlag = false
    }

    fun restart(){
        score = 0
        _screenState.value = ScreenState.Running
    }

    fun scorePercentage(): Int {
        val correctCount = score
        val questionCount = data.size
        return (correctCount * 100) / questionCount
    }

    private fun indexInc(){
        if (idx+1 < data.size){
            idx++
        } else {
            idx = 0
            data = data.shuffled()
            _screenState.value = ScreenState.Finished
        }
    }
}

class EyesightComparionViewModelFactory(private val comparisonDataRepository: EyesightComparisonRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EyesightComparisonViewModel::class.java)){
            return EyesightComparisonViewModel(comparisonDataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightComparisonViewModel")
    }
}