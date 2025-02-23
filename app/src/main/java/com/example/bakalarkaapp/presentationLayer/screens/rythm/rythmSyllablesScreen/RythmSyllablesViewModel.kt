package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmSyllablesScreen

import android.os.VibrationEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.ValidatableViewModel
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RythmSyllabUiState(
    val imgName: String,
    val soundName: String,
    val syllabCount: Int
)

class RythmSyllablesViewModel(app: LogoApp, levelIndex: Int) : ValidatableViewModel(app) {
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

    private val _buttonsStates = MutableStateFlow(List(4){ true })
    val buttonStates = _buttonsStates.asStateFlow()

    init {
        count = rounds.size - levelIndex
    }

    override fun validationCond(answer: IValidationAnswer): Boolean {
        return userSyllabSum == _uiState.value.syllabCount
    }

    override fun updateData() {
        currRound = rounds[roundIdx]
        userSyllabSum = 0
        _buttonsStates.update { l -> l.map { true } }
        _uiState.update { state ->
            state.copy(
                imgName = currRound.imageName,
                soundName = currRound.soundName,
                syllabCount = currRound.syllabCount
            )
        }
    }

    fun onItemClick(idx: Int) {
        viewModelScope.launch {
            // is enabled
            if (_buttonsStates.value[idx]){
                if (idx == 0 || !_buttonsStates.value[idx-1]){
                    userSyllabSum++
                    vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                    val newList = _buttonsStates.value.toMutableList()
                    newList[idx] = false
                    _buttonsStates.emit(newList.toList())
                }
            }
            // is not enabled
            else {
                if ((idx == 0 && !_buttonsStates.value[idx+1]) || idx == _buttonsStates.value.size-1 || _buttonsStates.value[idx+1]){
                    userSyllabSum--
                    val newList = _buttonsStates.value.toMutableList()
                    newList[idx] = true
                    _buttonsStates.emit(newList.toList())
                }
            }
        }
    }

    override fun scoreInc() {}
    override fun scoreDesc() {}
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