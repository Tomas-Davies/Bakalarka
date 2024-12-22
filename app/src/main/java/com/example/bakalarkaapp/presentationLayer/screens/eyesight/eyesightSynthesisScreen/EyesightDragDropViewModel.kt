package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthesisScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.shuffle
import com.example.bakalarkaapp.toDrawableId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class EyesightDragDropUiState(
    val wordResourcesId: String,
    val mixedWord: String,
    val currendWord: String
)

class EyesightDragDropViewModel(app: LogoApp, data: Array<String>) : BaseViewModel(app) {
    private val words = data.sortedBy { w -> w.length }
    private var word = words[roundIdx]
    private var wordMixed = word.uppercase().shuffle()
    private var userWord = CharArray(word.length)
    private var alreadyFailed = false

    private var _enabledStates = List(word.length) { mutableStateOf(true) }
    val enabledStates: List<MutableState<Boolean>> get() = _enabledStates

    private val _uiState =
        MutableStateFlow(EyesightDragDropUiState(word.toDrawableId(), wordMixed, word.uppercase()))
    val uiState: StateFlow<EyesightDragDropUiState> = _uiState.asStateFlow()

    private val _resetDropFlag = MutableStateFlow(false)
    val resetDropFlag: StateFlow<Boolean> = _resetDropFlag.asStateFlow()

    init {
        count = words.size
    }

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

    private fun scoreInc() {
        score++
    }

    private fun scoreDesc() {
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

    override fun updateData() {
        alreadyFailed = false
        if (nextRound()) {
            word = words[roundIdx]
            wordMixed = word.uppercase().shuffle()
            userWord = CharArray(word.length)
            _enabledStates = List(word.length) { mutableStateOf(true) }

            viewModelScope.launch {
                _resetDropFlag.emit(true)
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
                mixedWord = wordMixed,
                currendWord = word.uppercase()
            )
        }
    }

    override fun doRestart() {
        word = words[roundIdx]
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

class EyesightDragDropViewModelFactory(private val app: LogoApp, private val words: Array<String>) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightDragDropViewModel::class.java)) {
            return EyesightDragDropViewModel(app, words) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightComparisonViewModel")
    }
}