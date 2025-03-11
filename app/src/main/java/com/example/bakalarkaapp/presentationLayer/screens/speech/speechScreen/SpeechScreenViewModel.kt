package com.example.bakalarkaapp.presentationLayer.screens.speech.speechScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.SpeechLetter
import com.example.bakalarkaapp.dataLayer.repositories.SpeechRepo
import kotlinx.coroutines.launch

class SpeechViewModel(repo: SpeechRepo, app: LogoApp): BaseViewModel(app) {
    lateinit var lettersAndPositions: List<SpeechLetter>
        private set

    init {
        viewModelScope.launch {
            repo.loadData()
            lettersAndPositions = repo.data
            dataLoaded()
        }
    }
}


class SpeechViewModelFactory(
    private val repo: SpeechRepo,
    private val app: LogoApp
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

