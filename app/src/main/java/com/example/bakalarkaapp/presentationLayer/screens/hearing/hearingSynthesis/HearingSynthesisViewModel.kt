package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingSynthesis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.ValidatableViewModel
import com.example.bakalarkaapp.dataLayer.models.RoundContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HearingSynthesisUiState(
    val roundObjects: List<RoundContent>,
    val initObject: RoundContent
)

class HearingSynthesisViewModel(app: LogoApp) : ValidatableViewModel(app) {
    private val repo = app.hearingSynthesisRepository
    private val rounds = repo.data
    private var currentRound = rounds[roundIdx]
    private var currentObject = currentRound.objects
    private var spellingObject = currentObject.random()
    private var _uiState = MutableStateFlow(HearingSynthesisUiState(currentObject, spellingObject))
    val uiState = _uiState.asStateFlow()

    init {
        count = rounds.size
        viewModelScope.launch { _buttonsEnabled.emit(false) }
    }

    override fun validationCond(answer: IValidationAnswer): Boolean {
        if (answer is IValidationAnswer.StringAnswer) return answer.value == spellingObject.imgName
        throw IllegalArgumentException("$this expects answer of type String")
    }
    override suspend fun afterNewData() {}

    override fun updateData() {
        viewModelScope.launch {
            currentRound = rounds[roundIdx]
            currentObject = currentRound.objects
            spellingObject = currentObject.random()
            _uiState.update { state ->
                state.copy(
                    roundObjects = currentObject,
                    initObject = spellingObject
                )
            }
        }
    }

    override fun doRestart() {
        updateData()
    }

}

class HearingSynthesisViewModelFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingSynthesisViewModel::class.java)) {
            return HearingSynthesisViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}