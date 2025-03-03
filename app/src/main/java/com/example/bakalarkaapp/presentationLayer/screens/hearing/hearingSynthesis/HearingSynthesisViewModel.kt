package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingSynthesis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.ValidatableRoundViewModel
import com.example.bakalarkaapp.dataLayer.models.WordContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HearingSynthesisUiState(
    val roundObjects: List<WordContent>,
    val initObject: WordContent
)

class HearingSynthesisViewModel(app: LogoApp) : ValidatableRoundViewModel(app) {
    private val repo = app.hearingSynthesisRepository
    private val rounds = repo.data
    private var currentRound = rounds[roundIdx]
    private var currentObject = currentRound.objects
    private var spellingObject = currentObject.random()
    private var _uiState = MutableStateFlow(HearingSynthesisUiState(currentObject, spellingObject))
    val uiState = _uiState.asStateFlow()

    init {
        count = rounds.size
        _buttonsEnabled.update { false }
    }

    override fun validationCond(answer: IValidationAnswer?): Boolean {
        if (answer is IValidationAnswer.StringAnswer) return answer.value == spellingObject.imgName
        throw IllegalArgumentException("$this expects answer of type String")
    }

    override fun afterNewData() {}

    override fun updateData() {
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