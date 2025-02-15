package com.example.bakalarkaapp.presentationLayer.screens.speech.speechDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.utils.string.toDrawableId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SpeechDetailUiState(
    val wordResourcesId: String,
    val index: Int,
    val currentWord: String,
    val isOnFirstWord:Boolean,
    val isOnLastWord:Boolean
)

class SpeechDetailViewModel(private val words: Array<String>): ViewModel() {
    private var index = 0
    val count = words.size
    val word = words[index]
    private val _uiState = MutableStateFlow(SpeechDetailUiState(word.toDrawableId(), index, word, index == 0, index == words.size-1))
    val uiState: StateFlow<SpeechDetailUiState> = _uiState.asStateFlow()

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
                wordResourcesId = word.toDrawableId(),
                index = index,
                currentWord = word,
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


class SpeechDetailViewModelFactory(private val words: Array<String>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SpeechDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SpeechDetailViewModel(words) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: SpeechDetailViewModel")
    }
}

