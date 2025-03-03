package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.ValidatableRoundViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class EyesightMemoryUiState(
    var objectDrawableIds: List<String>,
    var round: Int
)

class EyesightMemoryViewModel(app: LogoApp) : ValidatableRoundViewModel(app) {
    private val memoryRepo = app.eyesightMemoryRepository
    private val data = memoryRepo.data
        .shuffled()
        .sortedBy { item -> item.objects.size }
    private var currentObjects = data[roundIdx].objects
        .map { obj -> obj.text ?: "" }
        .toMutableList()
    private var currentExtraObject = ""
    private var _uiState = MutableStateFlow(EyesightMemoryUiState(currentObjects, roundIdx + 1))
    val uiState = _uiState.asStateFlow()

    init {
        count = data.size
        chooseExtraObject()
        _buttonsEnabled.update { false }
    }

    override fun validationCond(answer: IValidationAnswer?): Boolean {
        if (answer is IValidationAnswer.StringAnswer) return answer.value == currentExtraObject
        throw IllegalArgumentException("$this expects answer of type String")
    }

    override fun afterNewData() {}

    private fun chooseExtraObject() {
        val randomIndex = Random.nextInt(currentObjects.size)
        currentExtraObject = currentObjects[randomIndex]
        currentObjects.removeAt(randomIndex)
    }

    override fun doRestart() {
        updateData()
    }

    fun showExtraItem() {
        currentObjects.add(currentExtraObject)
        _buttonsEnabled.update { true }
        _uiState.update { state ->
            state.copy(
                objectDrawableIds = currentObjects.shuffled(),
            )
        }
    }

    override fun updateData() {
        currentObjects = data[roundIdx].objects
            .map { obj -> obj.text ?: "" }
            .toMutableList()
        chooseExtraObject()
        _uiState.update { state ->
            state.copy(
                objectDrawableIds = currentObjects,
                round = roundIdx + 1
            )
        }
    }
}

class EyesightMemoryViewModelFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightMemoryViewModel::class.java)) {
            return EyesightMemoryViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}