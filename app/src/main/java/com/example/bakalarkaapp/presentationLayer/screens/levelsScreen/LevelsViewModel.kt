package com.example.bakalarkaapp.presentationLayer.screens.levelsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.repositories.ResourceMappedRepository

/**
 * Provides interface for rounds that will get navigated to from menu *level button* with image.
 * Also provides *key tags* for intent extras.
 *
 * @property imageName The name of the drawable resource file.
 */
interface ImageLevel {
    val imageName: String
    companion object {
        const val TAG = "LEVEL_INDEX"
        const val NEXT_CLASS_TAG = "NEXT_ACTIVITY_CLASS"
    }
}

/**
 * Manages data for buttons providing navigation to specific level.
 *
 * @param T The class that is going to be mapped.
 * @param R The specific type of ImageLevel being used.
 * @param app The [LogoApp] instance used by this viewModel.
 * @param repository The repository which provides list of rounds.
 * @param headingId The String resource id of heading used in [LevelsScreen]
 */
class LevelsViewModel<T, R: ImageLevel>(
    app: LogoApp,
    repository: ResourceMappedRepository<T, R>,
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


class LevelsViewModelFactory<R, S : ImageLevel>(
    private val app: LogoApp,
    private val repository: ResourceMappedRepository<R, S>,
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