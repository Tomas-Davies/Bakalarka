package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

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

data class HearingFonematicUiState(
    val objects: List<WordContent>,
    val playedObject: WordContent
)

class HearingFonematicViewModel(app: LogoApp) : RoundsViewModel(app) {
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
    override var roundSetSize = 5

    init {
        count = rounds.count()
        _buttonsEnabled.update { false }
    }

    fun onCardClick(drawableName: String){
        clickedCounterInc()
        viewModelScope.launch {
            if (drawableName == _uiState.value.playedObject.imageName){
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

    override fun doContinue() {
        super.doContinue()
        disableButtons()
    }


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