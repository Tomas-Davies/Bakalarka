package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.ValidatableViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EyesightComparisonUiState(
    val imageName: String,
    val answer: Boolean,
    val restartTrigger: Int = 0
)

class EyesightComparisonViewModel(
    app: LogoApp, private val levelIndex: Int
) : ValidatableViewModel(app) {
    init {
        roundIdx = levelIndex
    }
    private val comparisonDataRepository = app.eyesightComparisonRepository
    private var data = comparisonDataRepository.data
    private var currentItem = data[roundIdx]
    private val _uiState = MutableStateFlow(
        EyesightComparisonUiState(
            currentItem.imageName,
            currentItem.isSameShape
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        count = data.size
    }

    override fun validationCond(answer: IValidationAnswer): Boolean {
        if (answer is IValidationAnswer.BooleanAnswer){
            return answer.value == _uiState.value.answer
        }
        throw IllegalArgumentException("$this expects answer of type Boolean")
    }

    override fun scorePercentage(): Int {
        val count = count - levelIndex
        return (score * 100) / count
    }

    fun onTimerFinish(){
        viewModelScope.launch {
            delay(1500)
            _buttonsEnabled.value = true
            if (nextRound()) updateData()
            scoreDesc()
        }
    }

    public override fun updateData() {
        currentItem = data[roundIdx]
        _uiState.update { currentState ->
            currentState.copy(
                imageName = currentItem.imageName,
                answer = currentItem.isSameShape,
                restartTrigger = roundIdx
            )
        }
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