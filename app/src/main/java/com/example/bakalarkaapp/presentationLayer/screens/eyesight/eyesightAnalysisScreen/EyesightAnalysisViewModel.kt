package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightAnalysisScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.shuffle
import com.example.bakalarkaapp.toDrawableId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ScreenState {
    data object Running : ScreenState()
    data object Finished : ScreenState()
}

data class EyesightAnalysisUiState(
    val wordResourcesId: String,
    val currentWord: String
)

class EyesightAnalysisViewModel(private val words: Array<String>) : ViewModel() {
    private var index = 0
    private var word = words[index]
    private var wordMixed = word.uppercase().shuffle()
    private var userWord = CharArray(word.length)
    private var score = 0
    private var alreadyFailed = false

    private var _enabledStates = List(word.length) { mutableStateOf(true) }
    val enabledStates: List<MutableState<Boolean>> get() = _enabledStates

    private val _uiState = MutableStateFlow(EyesightAnalysisUiState(word.toDrawableId(), wordMixed))
    val uiState: StateFlow<EyesightAnalysisUiState> = _uiState.asStateFlow()

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _resetDropFlag = MutableStateFlow(false)
    val resetDropFlag: StateFlow<Boolean> = _resetDropFlag.asStateFlow()


    fun addLetterAt(letter: Char, index: Int) {
        if (index < word.length) {
            userWord[index] = letter
        }
    }

    fun removeLetterAt(index: Int) {
        if (index < word.length) {
            userWord[index] = ' '
        }
    }

    fun validateResult(): Boolean {
        if (userWord.joinToString("") == word.uppercase()) {
            scoreInc()
            updateData()
            return true
        } else {
            scoreDesc()
            resetData()
            return false
        }
    }

    private fun scoreInc(){
        score++
    }
    private fun scoreDesc(){
        if (!alreadyFailed) {
            score--
            alreadyFailed = true
        }
    }

    private fun resetData() {
        viewModelScope.launch {
            _resetDropFlag.emit(true)
            _enabledStates.forEach { item -> item.value = true }
            userWord = CharArray(word.length)
        }
    }

    private fun updateData() {
        alreadyFailed = false
        if (indexInc()){
            word = words[index]
            wordMixed = word.uppercase().shuffle()
            userWord = CharArray(word.length)

            viewModelScope.launch {
                _resetDropFlag.emit(true)
                _enabledStates = List(word.length) { mutableStateOf(true) }
                updateState()
            }
        }
    }

    fun setResetDropFlag(bool: Boolean) {
        viewModelScope.launch {
            _resetDropFlag.emit(bool)
        }
    }

    private fun updateState() {
        _uiState.update { currentState ->
            currentState.copy(
                wordResourcesId = word.toDrawableId(),
                currentWord = wordMixed
            )
        }
    }

    private fun indexInc(): Boolean {
        if (index + 1 < words.size) {
            index++
            return true
        } else {
            _screenState.value = ScreenState.Finished
            return false
        }
    }

    fun scorePercentage(): Int {
        val correctCount = score
        val questionCount = words.size
        return (correctCount * 100) / questionCount
    }

    fun restart() {
        index = 0
        score = 0
        word = words[index]
        wordMixed = word.uppercase().shuffle()
        userWord = CharArray(word.length)
        _enabledStates = List(word.length) { mutableStateOf(true) }

        viewModelScope.launch {
            _resetDropFlag.emit(true)
            _screenState.value = ScreenState.Running
            updateState()
            _resetDropFlag.emit(false)
        }
    }
}

class EyesightAnalysisViewModelFacory(private val words: Array<String>) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightAnalysisViewModel::class.java)) {
            return EyesightAnalysisViewModel(words) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightComparisonViewModel")
    }
}