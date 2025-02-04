package com.example.bakalarkaapp.presentationLayer.screens.levelsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.dataLayer.repositories.IRepository

interface LevelWithImage {
    val background: String
}


class LevelsViewModel<T: LevelWithImage>(repository: IRepository<T>): ViewModel() {
    private val data = repository.data
    val levels = getLevelsList()

    private fun getLevelsList(): List<String>{
        val l = mutableListOf<String>()
        for (round in data){
            val roundInfo = round.background
            l.add(roundInfo)
        }
        return l
    }
}


class LevelsViewModelFactory<R: LevelWithImage>(private val repository: IRepository<R>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LevelsViewModel::class.java)){
            return LevelsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}