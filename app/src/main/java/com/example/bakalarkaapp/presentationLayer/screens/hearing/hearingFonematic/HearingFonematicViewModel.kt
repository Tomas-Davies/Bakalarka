package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

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

data class HearingFonematicUiState(
    val objects: List<RoundContent>,
    val playedObject: RoundContent
)

class HearingFonematicViewModel(app: LogoApp) : ValidatableViewModel(app) {
    private val repo = app.hearingFonematicRepository
    private val rounds = repo.data.shuffled().sortedBy { round -> round.objects.size }
    private var currentRound = rounds[roundIdx]
    private var currentObject = currentRound.objects
    private val _uiState = MutableStateFlow(
        HearingFonematicUiState(
            objects = currentObject,
            playedObject = currentObject.random()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        count = rounds.count()
        viewModelScope.launch { _buttonsEnabled.emit(false) }
    }

    override fun validationCond(answer: IValidationAnswer): Boolean {
        if (answer is IValidationAnswer.StringAnswer) return (answer.value == _uiState.value.playedObject.imgName)
        throw IllegalArgumentException("$this expects answer of type String")
    }

    override suspend fun beforeNewData() {
        _buttonsEnabled.emit(false)
    }
    override suspend fun afterNewData() {}

    override fun updateData() {
        currentRound = rounds[roundIdx]
        currentObject = currentRound.objects

        _uiState.update { state ->
            state.copy(
                objects = currentObject,
                playedObject = currentObject.random()
            )
        }
    }

    override fun doRestart() {
        updateData()
    }
}

class HearingFonematicFactory(private val app: LogoApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingFonematicViewModel::class.java)) {
            return HearingFonematicViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}