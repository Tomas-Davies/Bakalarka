package com.example.bakalarkaapp.presentationLayer.screens.levelsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.repositories.XmlRepository

interface ImageLevel {
    val imageName: String
}


class LevelsViewModel<T, R: ImageLevel>(
    app: LogoApp,
    repository: XmlRepository<T, R>,
    val headingId: Int
): BaseViewModel(app) {
    private val data = repository.data
    val levels = getLevelsList()

    private fun getLevelsList(): List<String>{
        val l = mutableListOf<String>()
        data.forEach { round ->
            val roundInfo = round.imageName
            l.add(roundInfo)
        }
        return l
    }
}


class LevelsViewModelFactory<R, S: ImageLevel>(
    private val app: LogoApp,
    private val repository: XmlRepository<R, S>,
    private val headingId: Int
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LevelsViewModel::class.java)){
            return LevelsViewModel(app, repository, headingId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}