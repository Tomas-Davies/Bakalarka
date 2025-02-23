package com.example.bakalarkaapp.presentationLayer.screens.speech.speechScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.LogoApp

class SpeechViewModel(app: LogoApp): BaseViewModel(app) {
    private val repo = app.speechRepository
    val lettersAndPositions = repo.data
}


class SpeechViewModelFactory(private val app: LogoApp): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SpeechViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SpeechViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

