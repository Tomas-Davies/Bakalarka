package com.tomdev.logopadix.presentationLayer.screens.speech.speechFilterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.dataLayer.repositories.NormalizedWordContent
import com.tomdev.logopadix.dataLayer.repositories.SpeechRepo
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import com.tomdev.logopadix.utils.string.endsWithCh
import com.tomdev.logopadix.utils.string.normalizedWithCh
import com.tomdev.logopadix.utils.string.startsWithCh
import com.tomdev.logopadix.viewModels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SpeechFilterViewModel(
    val app: LogoApp,
    val repo: SpeechRepo
): BaseViewModel(app) {
    private val dailyActivityRepo = app.dailyActivityRepo
    private var processedWords = MutableStateFlow<List<NormalizedWordContent>?>(null)
    private var _filteredWords =  MutableStateFlow(emptyList<NormalizedWordContent>())
    val filteredWords = _filteredWords.asStateFlow()


    init {
        viewModelScope.launch {
            repo.loadData()
            processedWords.value = repo.getProcessedWords()

        }
        dailyActivityRepo.markPracticed()
    }

    fun Clear(){
        _filteredWords.value = emptyList()
    }

    fun GetWords(contains: String, exclude: String, starts: String, ends: String){
        _screenState.value = ScreenState.Loading
        val union = contains+exclude+starts+ends
        if (union.isEmpty()) Clear()
        else {
            viewModelScope.launch {
                val processed = processedWords.filterNotNull().first()
                val normalizedContains = contains.normalizedWithCh()
                val normalizedExclude = exclude.normalizedWithCh()

                _filteredWords.value = processed.filter { word ->
                    val containCond = normalizedContains.isEmpty() || word.letters.containsAll(normalizedContains)
                    val excludeCond = normalizedExclude.isEmpty() || word.letters.none{ it in normalizedExclude }
                    val textLower = word.text.lowercase()
                    containCond && excludeCond
                    && textLower.startsWithCh(starts)
                    && textLower.endsWithCh(ends)
                }
                _screenState.value = ScreenState.Success
            }
        }
    }
}


class SpeechFilterViewModelFactory(
    private val app: LogoApp,
    private val repo: SpeechRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SpeechFilterViewModel::class.java)){
            return SpeechFilterViewModel(app, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class $modelClass")
    }
}