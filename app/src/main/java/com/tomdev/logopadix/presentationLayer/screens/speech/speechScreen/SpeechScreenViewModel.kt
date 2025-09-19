package com.tomdev.logopadix.presentationLayer.screens.speech.speechScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.viewModels.BaseViewModel
import com.tomdev.logopadix.dataLayer.repositories.SpeechLetter
import com.tomdev.logopadix.dataLayer.repositories.SpeechRepo
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import kotlinx.coroutines.launch

class SpeechViewModel(repo: SpeechRepo, app: com.tomdev.logopadix.LogoApp): BaseViewModel(app) {
    lateinit var lettersAndPositions: List<SpeechLetter>
        private set

    init {
        viewModelScope.launch {
            repo.loadData()
            lettersAndPositions = repo.data
            _screenState.value = ScreenState.Success
        }
    }
}


class SpeechViewModelFactory(
    private val repo: SpeechRepo,
    private val app: com.tomdev.logopadix.LogoApp
): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SpeechViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SpeechViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

