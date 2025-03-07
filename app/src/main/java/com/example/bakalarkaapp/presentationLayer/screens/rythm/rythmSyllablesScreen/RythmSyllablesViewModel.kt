package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmSyllablesScreen

import android.os.VibrationEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RythmSyllabUiState(
    val imageName: String,
    val soundName: String,
    val syllabCount: Int
)

class RythmSyllablesViewModel(app: LogoApp, levelIndex: Int) : RoundsViewModel(app) {
    init {
        roundIdx = levelIndex
    }

    private val repo = app.rythmSyllablesRepository
    val rounds = repo.data

    private var currRound = rounds[roundIdx]

    private val _uiState = MutableStateFlow(
        RythmSyllabUiState(currRound.imageName, currRound.soundName, currRound.syllabCount)
    )
    val uiState = _uiState.asStateFlow()
    private var userSyllabSum: Int = 0

    private val _buttonsStates = MutableStateFlow(List(4) { false })
    val buttonStates = _buttonsStates.asStateFlow()
    override var roundSetSize = 5

    init {
        count = rounds.size
    }

    fun onBtnClick(){
        viewModelScope.launch {
            clickedCounterInc()
            if (userSyllabSum == _uiState.value.syllabCount){
                onCorrectAnswer()
            } else {
                onWrongAnswer()
            }
        }
    }


    private suspend fun onCorrectAnswer(){
        playOnCorrectSound()
        showCorrectMessage()
        scoreInc()
        roundsCompletedInc()
        if (roundSetCompletedCheck()) {
            showRoundSetDialog()
        } else {
            onContinue()
        }
    }


    private suspend fun onWrongAnswer(){
        playOnWrongSound()
        showWrongMessage()
    }


    override fun updateData() {
        currRound = rounds[roundIdx]
        userSyllabSum = 0
        _buttonsStates.update { l -> l.map { false } }
        _uiState.update { state ->
            state.copy(
                imageName = currRound.imageName,
                soundName = currRound.soundName,
                syllabCount = currRound.syllabCount
            )
        }
    }


    fun onItemClick(idx: Int) {
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        // not selected -> selected
        if (!_buttonsStates.value[idx]) {
            if (idx == 0 || _buttonsStates.value[idx - 1]) {
                userSyllabSum++
                val newList = _buttonsStates.value.toMutableList()
                newList[idx] = true
                _buttonsStates.update { newList.toList() }
            }
        }
        // selected -> not selected
        else {
            if (
                (idx == 0 && !_buttonsStates.value[1])
                || (idx == _buttonsStates.value.size - 1)
                || (idx != 0 && !_buttonsStates.value[idx + 1])
            ) {
                userSyllabSum--
                val newList = _buttonsStates.value.toMutableList()
                newList[idx] = false
                _buttonsStates.update { newList.toList() }
            }
        }
    }
}

class RythmSyllablesViewModelFactory(private val app: LogoApp, private val levelIndex: Int) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RythmSyllablesViewModel::class.java)) {
            return RythmSyllablesViewModel(app, levelIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}