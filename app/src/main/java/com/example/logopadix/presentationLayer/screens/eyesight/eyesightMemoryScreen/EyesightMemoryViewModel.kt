package com.example.logopadix.presentationLayer.screens.eyesight.eyesightMemoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.logopadix.LogoApp
import com.example.logopadix.dataLayer.repositories.BasicWordsRound
import com.example.logopadix.dataLayer.repositories.BasicWordsRepo
import com.example.logopadix.presentationLayer.states.ScreenState
import com.example.logopadix.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random


data class EyesightMemoryUiState(
    var objectDrawableIds: List<String>,
    var round: Int
)

class EyesightMemoryViewModel(
    private val repo: BasicWordsRepo,
    app: LogoApp
) : RoundsViewModel(app)
{
    private lateinit var _uiState: MutableStateFlow<EyesightMemoryUiState>
    lateinit var uiState: StateFlow<EyesightMemoryUiState>
        private set

    private lateinit var data: List<BasicWordsRound>
    private lateinit var currentObjects: MutableList<String>
    private lateinit var currentExtraObject: String
    override var roundSetSize = 3

    init {
        viewModelScope.launch {
            repo.loadData()
            data = repo.data
                .shuffled()
                .sortedBy { item -> item.objects.size }

            currentObjects = data[roundIdx].objects
                .map { obj -> obj.text ?: "" }
                .toMutableList()
            _uiState = MutableStateFlow(EyesightMemoryUiState(currentObjects, roundIdx + 1))
            uiState = _uiState
            count = data.size
            chooseExtraObject()
            disableButtons()
            _screenState.value = ScreenState.Success
        }
    }


    suspend fun onCardClick(name: String): Boolean {
        clickedCounterInc()
        return if (name == currentExtraObject){
            onCorrectAnswer()
            true
        } else {
            onWrongAnswer()
            false
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


    private fun chooseExtraObject() {
        val randomIndex = Random.nextInt(currentObjects.size)
        currentExtraObject = currentObjects[randomIndex]
        currentObjects.removeAt(randomIndex)
    }


    fun showExtraItem() {
        currentObjects.add(currentExtraObject)
        enableButtons()
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


class EyesightMemoryViewModelFactory(
    private val repo: BasicWordsRepo,
    private val app: LogoApp
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightMemoryViewModel::class.java)) {
            return EyesightMemoryViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}