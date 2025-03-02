package com.example.bakalarkaapp.presentationLayer.screens.speech.speechDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.WordContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SpeechDetailUiState(
    val currentWord: WordContent,
    val index: Int,
    val isOnFirstWord:Boolean,
    val isOnLastWord:Boolean
)

class SpeechDetailViewModel(app: LogoApp, letterLabel: String, posLabel: String): BaseViewModel(app) {
    private val repo = app.speechRepository
    private val words = repo.getWords(letterLabel, posLabel) ?: listOf(WordContent())
    private var index = 0
    val count = words.size
    private val word = words[index]
    private val _uiState = MutableStateFlow(SpeechDetailUiState(word, index, index == 0, index == words.size-1))
    val uiState = _uiState.asStateFlow()

    fun next(){
        if (indexInc()){
            updateState()
        }
    }
    fun previous(){
        if (indexDec()){
            updateState()
        }
    }

    private fun updateState(){
        _uiState.update { currentState ->
            val word = words[index]
            currentState.copy(
                currentWord = word,
                index = index,
                isOnFirstWord = index == 0,
                isOnLastWord = index == words.size-1
            )
        }
    }

    private fun indexInc(): Boolean {
        if (index+1 < words.size){
            index++
            return true
        }
        return false
    }
    private fun indexDec(): Boolean {
        if (index > 0){
            index--
            return true
        }
        return false
    }
}


class SpeechDetailViewModelFactory(
    private val app: LogoApp,
    private val letterLabel: String,
    private val posLabel: String
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SpeechDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SpeechDetailViewModel(app, letterLabel, posLabel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

