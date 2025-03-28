package com.example.logopadix.presentationLayer.screens.hearing.hearingSynthesis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.logopadix.LogoApp
import com.example.logopadix.dataLayer.repositories.BasicWordsRound
import com.example.logopadix.dataLayer.WordContent
import com.example.logopadix.dataLayer.repositories.BasicWordsRepo
import com.example.logopadix.presentationLayer.states.ScreenState
import com.example.logopadix.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HearingSynthesisUiState(
    val roundObjects: List<WordContent>,
    val initObject: WordContent
)

class HearingSynthesisViewModel(
    private val repo: BasicWordsRepo,
    app: LogoApp
) : RoundsViewModel(app)
{
    private lateinit var rounds: List<BasicWordsRound>
    private lateinit var currentRound: BasicWordsRound
    private lateinit var currentObject: List<WordContent>
    private lateinit var spellingObject: WordContent
    private lateinit var _uiState: MutableStateFlow<HearingSynthesisUiState>
    lateinit var uiState: StateFlow<HearingSynthesisUiState>
        private set

    override var roundSetSize = 3

    init {
        viewModelScope.launch {
            repo.loadData()
            rounds = repo.data.shuffled().sortedBy { item -> item.objects.size }
            count = rounds.size
            currentRound = rounds[roundIdx]
            currentObject = currentRound.objects
            spellingObject = currentObject.random()
            _uiState = MutableStateFlow(HearingSynthesisUiState(currentObject, spellingObject))
            uiState = _uiState
            _buttonsEnabled.value = false
            _screenState.value = ScreenState.Success
        }
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

class HearingSynthesisViewModelFactory(
    private val repo: BasicWordsRepo,
    private val app: LogoApp
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearingSynthesisViewModel::class.java)) {
            return HearingSynthesisViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}