package com.tomdev.logopadix.presentationLayer.screens.hearing.hearingAssigning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.dataLayer.WordContent
import com.tomdev.logopadix.dataLayer.repositories.BasicWordsRepo
import com.tomdev.logopadix.dataLayer.repositories.BasicWordsRound
import com.tomdev.logopadix.presentationLayer.states.ScreenState
import com.tomdev.logopadix.viewModels.DifficultyRoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HearingAssignUiState(
    val roundObjects: List<WordContent>,
    val chosenObject: WordContent
)

class HearingAssignViewModel(
    private val repo: BasicWordsRepo,
    app: com.tomdev.logopadix.LogoApp,
    private val diff: String
) : DifficultyRoundsViewModel(diff, app) {
    private lateinit var rounds: List<BasicWordsRound>
    private lateinit var currentRound: BasicWordsRound
    private lateinit var currentObject: List<WordContent>
    private lateinit var chosenObject: WordContent
    private lateinit var _uiState: MutableStateFlow<HearingAssignUiState>
    lateinit var uiState: StateFlow<HearingAssignUiState>
        private set

    override var roundSetSize = 3

    init {
        viewModelScope.launch {
            repo.loadData()
            rounds = if (diff.isNotEmpty()) {
                repo.data
                    .filter { item -> item.difficulty == diff }
                    .shuffled()
            } else {
                repo.data
            }
            count = rounds.size
            currentRound = rounds[roundIdx]
            currentObject = currentRound.objects
            chosenObject = currentObject.random()
            _uiState = MutableStateFlow(HearingAssignUiState(currentObject, chosenObject))
            uiState = _uiState
            _buttonsEnabled.value = false
            _screenState.value = ScreenState.Success
        }
    }


    fun onCardClick(answer: String){
        viewModelScope.launch {
            clickedCounterInc()
            if (answer == chosenObject.imageName){
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
        chosenObject = currentObject.random()
        _uiState.update { state ->
            state.copy(
                roundObjects = currentObject,
                chosenObject = chosenObject
            )
        }
    }
}


class HearingAssignFactory(
    private val repo: BasicWordsRepo,
    private val app: com.tomdev.logopadix.LogoApp,
    private val diff: String
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingAssignViewModel::class.java)){
            return HearingAssignViewModel(repo, app, diff) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}