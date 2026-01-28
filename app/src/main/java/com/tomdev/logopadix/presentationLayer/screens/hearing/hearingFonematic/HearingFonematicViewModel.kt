package com.tomdev.logopadix.presentationLayer.screens.hearing.hearingFonematic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.dataLayer.repositories.BasicWordsRound
import com.tomdev.logopadix.dataLayer.WordContent
import com.tomdev.logopadix.dataLayer.repositories.BasicWordsRepo
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import com.tomdev.logopadix.services.DayStreakService
import com.tomdev.logopadix.viewModels.RoundsViewModel
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
    app: com.tomdev.logopadix.LogoApp,
    streakService: DayStreakService
) : RoundsViewModel(app, streakService)
{
    private lateinit var rounds: List<BasicWordsRound>
    private lateinit var currentRound: BasicWordsRound
    private lateinit var currentObjects: List<WordContent>
    private lateinit var _uiState: MutableStateFlow<HearingFonematicUiState>
    lateinit var uiState: StateFlow<HearingFonematicUiState>
        private set

    override var roundSetSize = 5

    init {
        viewModelScope.launch {
            var loadedData = repo.loadData()
            rounds = loadedData.shuffled().sortedBy { round -> round.objects.size }
            count = rounds.count()
            currentRound = rounds[roundIdx]
            currentObjects = currentRound.objects
            _uiState = MutableStateFlow(HearingFonematicUiState(currentObjects.shuffled(), currentObjects.random()))
            uiState = _uiState
            _buttonsEnabled.value = false
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
        currentObjects = currentRound.objects

        _uiState.update { state ->
            state.copy(
                objects = currentObjects.shuffled(),
                playedObject = currentObjects.random()
            )
        }
    }
}

class HearingFonematicFactory(
    private val repo: BasicWordsRepo,
    private val app: com.tomdev.logopadix.LogoApp,
    private val streakService: DayStreakService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingFonematicViewModel::class.java)) {
            return HearingFonematicViewModel(repo, app, streakService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}