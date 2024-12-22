package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EyesightComparisonUiState(
    val imageId: String,
    val answer: Boolean,
    val restartTrigger: Int = 0
)

class EyesightComparisonViewModel(app: LogoApp) : BaseViewModel(app) {
    private val comparisonDataRepository = app.eyesightComparisonRepository
    private var data = comparisonDataRepository.data.shuffled()
    private var currentItem = data[0]
    private val _uiState = MutableStateFlow(
        EyesightComparisonUiState(
            currentItem.imageId.value,
            currentItem.isSameShape.value.toBoolean()
        )
    )
    val uiState: StateFlow<EyesightComparisonUiState> = _uiState.asStateFlow()
    private var btnOneClickedFlag: Boolean = false
    private var btnTwoClickedFlag: Boolean = false

    init {
        count = data.size
    }

    fun scoreInc() {
        if (!btnOneClickedFlag) {
            score++
            btnOneClickedFlag = true
        }
    }

    private fun scoreDesc() {
        if (!btnTwoClickedFlag) {
            score--
            btnTwoClickedFlag = true
        }
    }

    private fun onCorrectAnswer(){
        if (nextRound()) updateData()
    }

    fun validateAnswer(answer: Boolean): Boolean {
        if (answer == _uiState.value.answer){
            scoreInc()
            onCorrectAnswer()
            return true
        }
        scoreDesc()
        return false
    }

    public override fun updateData() {
        currentItem = data[roundIdx]
        println(currentItem.imageId.value)
        _uiState.update { currentState ->
            currentState.copy(
                imageId = currentItem.imageId.value,
                answer = currentItem.isSameShape.value.toBoolean(),
                restartTrigger = roundIdx
            )
        }
        btnOneClickedFlag = false
        btnTwoClickedFlag = false
    }

    override fun doRestart() {
        data = data.shuffled()
        updateData()
    }
}

class EyesightComparionViewModelFactory(private val app: LogoApp) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EyesightComparisonViewModel::class.java)) {
            return EyesightComparisonViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightComparisonViewModel")
    }
}