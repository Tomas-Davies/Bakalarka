package com.example.bakalarkaapp.presentationLayer.screens.eyesight.imageSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.LogoApp

class LevelsViewModel(app: LogoApp): ViewModel() {
    private val data = app.eyesightSearchRepository.data
    val levels = getSearchLevels()


    private fun getSearchLevels(): List<Pair<String, Int>>{
        val l = mutableListOf<Pair<String, Int>>()
        for (round in data){
            val roundInfo = Pair(round.background.value, round.items.size)
            l.add(roundInfo)
        }
        return l
    }
}

class LevelsViewModelFactory(private val app: LogoApp): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LevelsViewModel::class.java)){
            return LevelsViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}