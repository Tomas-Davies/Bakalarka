package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingSynthesis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.WordContent
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HearingSynthesisUiState(
    val roundObjects: List<WordContent>,
    val initObject: WordContent
)

class HearingSynthesisViewModel(app: LogoApp) : RoundsViewModel(app) {
    private val repo = app.hearingSynthesisRepository
    private val rounds = repo.data
    private var currentRound = rounds[roundIdx]
    private var currentObject = currentRound.objects
    private var spellingObject = currentObject.random()
    private var _uiState = MutableStateFlow(HearingSynthesisUiState(currentObject, spellingObject))
    val uiState = _uiState.asStateFlow()
    override var roundSetSize = 3
    init {
        count = rounds.size
        _buttonsEnabled.update { false }
    }

    fun onCardClick(answer: String){
        viewModelScope.launch {
            clickedCounterInc()
            if (answer == spellingObject.imageName){
                onCorrectAnswer()
            } else {
                onWrongAnswer()
            }
        }
    }

    private suspend fun onCorrectAnswer(){
        disableButtons()
        playOnCorrectSound()
        showCorrectMessage()
        scoreInc()
        roundsCompletedInc()

        if (roundSetCompletedCheck()){
            showRoundSetDialog()
        } else {
            doContinue()
        }
    }

    private suspend fun onWrongAnswer(){
        playOnWrongSound()
        showWrongMessage()
    }

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