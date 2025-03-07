package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.RoundsViewModel
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
    app: LogoApp, levelIndex: Int
) : RoundsViewModel(app) {
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
    override var roundSetSize = 5

    init {
        count = data.size
    }

    fun onBtnClick(answer: Boolean){
        viewModelScope.launch {
            clickedCounterInc()
            if (answer == _uiState.value.answer){
                onCorrectAnswer()
            } else {
                onWrongAnswer()
            }
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
        enableButtons()
    }

    fun onTimerFinish(){
        if (!roundSetCompletedCheck()){
            viewModelScope.launch {
                playOnWrongSound()
                delay(1500)
                doContinue()
                clickCounter++
            }
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