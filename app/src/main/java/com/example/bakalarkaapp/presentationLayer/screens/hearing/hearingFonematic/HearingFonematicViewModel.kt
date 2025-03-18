package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.BasicWordsRound
import com.example.bakalarkaapp.dataLayer.models.WordContent
import com.example.bakalarkaapp.dataLayer.repositories.BasicWordsRepo
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import com.example.bakalarkaapp.viewModels.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HearingFonematicUiState(
    val objects: List<WordContent>,
    val playedObject: WordContent
)

class HearingFonematicViewModel(
    private val repo: BasicWordsRepo,
    app: LogoApp
) : RoundsViewModel(app)
{
    private lateinit var rounds: List<BasicWordsRound>
    private lateinit var currentRound: BasicWordsRound
    private lateinit var currentObject: List<WordContent>
    private lateinit var _uiState: MutableStateFlow<HearingFonematicUiState>
    lateinit var uiState: StateFlow<HearingFonematicUiState>
        private set

    override var roundSetSize = 5

    init {
        viewModelScope.launch {
            repo.loadData()
            rounds = repo.data.shuffled().sortedBy { round -> round.objects.size }
            count = rounds.count()
            currentRound = rounds[roundIdx]
            currentObject = currentRound.objects
            _uiState = MutableStateFlow(HearingFonematicUiState(currentObject, currentObject.random()))
            uiState = _uiState
            _buttonsEnabled.update { false }
            _screenState.value = ScreenState.Success
        }
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

class HearingFonematicFactory(
    private val repo: BasicWordsRepo,
    private val app: LogoApp
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingFonematicViewModel::class.java)) {
            return HearingFonematicViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}