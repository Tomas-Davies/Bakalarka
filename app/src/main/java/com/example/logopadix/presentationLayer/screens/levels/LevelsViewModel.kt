package com.example.logopadix.presentationLayer.screens.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.logopadix.viewModels.BaseViewModel
import com.example.logopadix.LogoApp
import com.example.logopadix.dataLayer.IModel
import com.example.logopadix.dataLayer.repositories.ResourceMappedRepository
import com.example.logopadix.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Provides interface for rounds that will get navigated to from menu *level button* with image.
 * Also provides *key tags* for intent extras.
 *
 * @property imageName The name of the drawable resource file.
 */
interface IImageLevel {
    val imageName: String
    companion object {
        const val TAG = "LEVEL_INDEX"
        const val NEXT_CLASS_TAG = "NEXT_ACTIVITY_CLASS"
        const val LEVEL_ITEM_LABEL_ID_TAG = "LEVEL_ITEM_LABEL"
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
class LevelsViewModel<T : IModel<R>, R : IImageLevel>(
    app: LogoApp,
    repository: ResourceMappedRepository<T, R>,
    val headingId: Int
): BaseViewModel(app) {

    private var data: List<R> = emptyList()
    private var _levels = MutableStateFlow<List<String>>(emptyList())
    var levels = _levels.asStateFlow()

    init {
        viewModelScope.launch {
            repository.loadData()
            data = repository.data
            _levels.value = getLevelsList()
            _screenState.value = ScreenState.Success
        }
    }

    private fun getLevelsList(): List<String>{
        val l = mutableListOf<String>()
        data.forEach { round ->
            val roundInfo = round.imageName
            l.add(roundInfo)
        }
        return l
    }
}


class LevelsViewModelFactory<R : IModel<S>, S : IImageLevel>(
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