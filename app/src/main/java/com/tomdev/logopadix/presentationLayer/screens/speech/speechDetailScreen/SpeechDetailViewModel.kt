package com.tomdev.logopadix.presentationLayer.screens.speech.speechDetailScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.viewModels.BaseViewModel
import com.tomdev.logopadix.dataLayer.UserSentence
import com.tomdev.logopadix.dataLayer.WordContent
import com.tomdev.logopadix.dataLayer.repositories.SpeechRepo
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpeechDetailWordsUiState(
    val currentWord: WordContent,
    val index: Int,
    val isOnFirstWord: Boolean,
    val isOnLastWord: Boolean
)

class SpeechDetailViewModel(
    private val repo: SpeechRepo,
    private val letterLabel: String,
    posLabel: String,
    app: com.tomdev.logopadix.LogoApp
) : BaseViewModel(app) {
    private lateinit var words: List<WordContent>
    private var index = 0
    var count = 0
        private set

    private lateinit var word: WordContent
    private lateinit var _uiState: MutableStateFlow<SpeechDetailWordsUiState>
    lateinit var uiState: StateFlow<SpeechDetailWordsUiState>
        private set
    lateinit var defaultSentences: List<String>
        private set

    val userSentences = repo.getUserSentences(letterLabel)
        .flowOn(Dispatchers.IO)
        .catch { e ->
            e.printStackTrace()
            Log.e(
                "Error getting sentences",
                "Message: ${e.message}\nCause: ${e.cause}\nStack trace: ${e.stackTraceToString()}"
            )
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch {
            words = repo.getWords(letterLabel, posLabel) ?: listOf(WordContent())
            count = words.size
            word = words[index]
            _uiState = MutableStateFlow(
                SpeechDetailWordsUiState(
                    word,
                    index,
                    index == 0,
                    index == words.size - 1
                )
            )
            uiState = _uiState
            _screenState.value = ScreenState.Success
            defaultSentences = repo.getDefaultSentences(letterLabel) ?: emptyList()
        }
    }


    fun addUserSentence(text: String) {
        val userSentence = UserSentence(0, letterLabel, text)
        viewModelScope.launch(Dispatchers.IO) {
            repo.addUserSentence(userSentence)
        }
    }


    fun deleteUserSentence(userSentence: UserSentence) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteUserSentence(userSentence)
        }
    }


    private fun updateUserSentence(userSentence: UserSentence) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateUserSentence(userSentence)
        }
    }


    fun onSaveButtonClick(newText: String, userSentence: UserSentence){
        if (newText != userSentence.sentence || newText.isBlank()) {
            if (newText.isNotBlank()) {
                val newUserSentence = userSentence.copy(
                    sentence = newText
                )
                updateUserSentence(newUserSentence)
            } else {
                deleteUserSentence(userSentence)
            }
        }
    }


    fun next() {
        if (indexInc()) updateState()
    }


    fun previous() {
        if (indexDec()) updateState()
    }


    private fun updateState() {
        _uiState.update { currentState ->
            val word = words[index]
            currentState.copy(
                currentWord = word,
                index = index,
                isOnFirstWord = index == 0,
                isOnLastWord = index == words.size - 1
            )
        }
    }


    private fun indexInc(): Boolean {
        if (index + 1 < words.size) {
            index++
            return true
        }
        return false
    }


    private fun indexDec(): Boolean {
        if (index > 0) {
            index--
            return true
        }
        return false
    }
}


class SpeechDetailViewModelFactory(
    private val repo: SpeechRepo,
    private val letterLabel: String,
    private val posLabel: String,
    private val app: com.tomdev.logopadix.LogoApp
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpeechDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpeechDetailViewModel(repo, letterLabel, posLabel, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

