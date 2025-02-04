package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EyesightComparisonUiState(
    val imageId: String,
    val answer: Boolean,
    val restartTrigger: Int = 0
)

class EyesightComparisonViewModel(app: LogoApp, private val levelIndex: Int) : BaseViewModel(app) {
    init {
        roundIdx = levelIndex
    }

    private val comparisonDataRepository = app.eyesightComparisonRepository
    private var data = comparisonDataRepository.data
    private var currentItem = data[roundIdx]
    private val _uiState = MutableStateFlow(
        EyesightComparisonUiState(
            currentItem.background,
            currentItem.isSameShape
        )
    )
    val uiState: StateFlow<EyesightComparisonUiState> = _uiState.asStateFlow()
    private var btnOneClickedFlag: Boolean = false
    private var btnTwoClickedFlag: Boolean = false

    init {
        count = data.size
    }

    private fun scoreInc() {
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

    private fun moveNext(){
        viewModelScope.launch {
            delay(1500)
            _buttonsEnabled.value = true
            if (nextRound()) {
                updateData()
            }
        }
    }

    override fun scorePercentage(): Int {
        val count = count - levelIndex
        return (score * 100) / count
    }

    fun onTimerFinish(){
        moveNext()
        scoreDesc()
    }

    fun validateAnswer(answer: Boolean): Boolean {
        if (answer == _uiState.value.answer){
            _buttonsEnabled.value = false
            showMessage(result = true)
            scoreInc()
            moveNext()
            return true
        }
        showMessage(result = false)
        scoreDesc()
        return false
    }

    public override fun updateData() {
        currentItem = data[roundIdx]
        _uiState.update { currentState ->
            currentState.copy(
                imageId = currentItem.background,
                answer = currentItem.isSameShape,
                restartTrigger = roundIdx
            )
        }
        btnOneClickedFlag = false
        btnTwoClickedFlag = false
    }

    override fun doRestart() {
        roundIdx = levelIndex
        updateData()
    }
}

class EyesightComparionViewModelFactory(private val app: LogoApp, private val levelIndex: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EyesightComparisonViewModel::class.java)) {
            return EyesightComparisonViewModel(app, levelIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}