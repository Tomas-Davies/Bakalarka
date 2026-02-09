package com.tomdev.logopadix.presentationLayer.screens.rythm.rythmSyllablesScreen

import android.os.VibrationEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.dataLayer.repositories.RythmSyllabRound
import com.tomdev.logopadix.dataLayer.repositories.RythmSyllablesRepo
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import com.tomdev.logopadix.viewModels.DifficultyRoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class RythmSyllabUiState(
    val imageName: String,
    val soundName: String,
    val syllabCount: Int
)

class RythmSyllablesViewModel(
    private val repo: RythmSyllablesRepo,
    app: com.tomdev.logopadix.LogoApp,
    levelIndex: Int,
    diff: String,
) : DifficultyRoundsViewModel(diff, app)
{
    lateinit var rounds: List<RythmSyllabRound>
    private lateinit var currRound: RythmSyllabRound

    private lateinit var _uiState: MutableStateFlow<RythmSyllabUiState>
    lateinit var uiState: StateFlow<RythmSyllabUiState>
        private set

    private var userSyllabSum: Int = 0
    private val _buttonsStates = MutableStateFlow(List(4) { false })
    val buttonStates = _buttonsStates.asStateFlow()
    override var roundSetSize = 5

    init {
        roundIdx = levelIndex
        viewModelScope.launch {
            var loadedData = repo.loadData()
            rounds = if (diff.isNotEmpty()) {
                loadedData.filter { item -> item.difficulty == diff }
            } else {
                loadedData
            }
            count = rounds.size
            currRound = rounds[roundIdx]
            _uiState = MutableStateFlow(RythmSyllabUiState(currRound.imageName, currRound.soundName, currRound.syllabCount))
            uiState = _uiState
            _screenState.value = ScreenState.Success
        }
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
            doContinue()
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

class RythmSyllablesViewModelFactory(
    private val repo: RythmSyllablesRepo,
    private val app: com.tomdev.logopadix.LogoApp,
    private val levelIndex: Int,
    private val diff: String
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RythmSyllablesViewModel::class.java)) {
            return RythmSyllablesViewModel(repo, app, levelIndex, diff) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}