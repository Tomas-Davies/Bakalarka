package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.DifferItem
import com.example.bakalarkaapp.dataLayer.repositories.EyesightDifferRepo
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class EyesightDifferUiState(
    val imageName: String,
    val answers: List<String>,
    val correctAnswers: List<String>,
    val question: String
)


class EyesightDifferViewModel(
    private val repo: EyesightDifferRepo,
    app: LogoApp, levelIndex: Int
) : RoundsViewModel(app)
{
    private lateinit var data: List<DifferItem>
    private var questionIdx = 0
    private lateinit var currentItem: DifferItem
    private var _questionNumber = MutableStateFlow(1)
    val questionNumber = _questionNumber.asStateFlow()
    private lateinit var _questionCountInRound: MutableStateFlow<Int>
    lateinit var questionCountInRound: StateFlow<Int>
    private lateinit var _uiState: MutableStateFlow<EyesightDifferUiState>
    lateinit var uiState: StateFlow<EyesightDifferUiState>
        private set

    override var roundSetSize = 2

    init {
        roundIdx = levelIndex
        viewModelScope.launch {
            repo.loadData()
            data = repo.data
            count = data.size
            currentItem = data[roundIdx]
            _questionCountInRound = MutableStateFlow(currentItem.rounds.size)
            questionCountInRound = _questionCountInRound.asStateFlow()
            _uiState = MutableStateFlow(
                EyesightDifferUiState(
                    imageName = currentItem.imageName,
                    answers = getPossibleAnswers(),
                    correctAnswers = getCorrectAnswers(),
                    question = getQuestion()
                )
            )
            uiState = _uiState.asStateFlow()
            dataLoaded()
        }
    }


    fun onBtnClick(answer: String){
        viewModelScope.launch {
            clickedCounterInc()
            if (answer in getCorrectAnswers()){
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
        if (!hasNextQuestion()) {
            roundsCompletedInc()
            if (roundSetCompletedCheck()) {
                showRoundSetDialog()
            } else {
                doContinue()
            }
        } else {
            updateQuestion()
            enableButtons()
        }
    }


    private suspend fun onWrongAnswer(){
        playOnWrongSound()
        showWrongMessage()
    }


    override fun doContinue() {
        super.doContinue()
        enableButtons()
    }


    private fun hasNextQuestion(): Boolean {
        val questions = currentItem.rounds
        return questionIdx < questions.size - 1
    }


    private fun updateQuestion() {
        _questionNumber.update { _questionNumber.value + 1 }
        questionIdx++
        _uiState.update { currentState ->
            currentState.copy(
                correctAnswers = getCorrectAnswers(),
                question = getQuestion()
            )
        }
    }


    override fun updateData() {
        currentItem = data[roundIdx]
        questionIdx = 0
        _questionNumber.update { 1 }
        _questionCountInRound.update { currentItem.rounds.size }
        _uiState.update { currentState ->
            currentState.copy(
                imageName = currentItem.imageName,
                answers = getPossibleAnswers(),
                correctAnswers = getCorrectAnswers(),
                question = getQuestion()
            )
        }
    }


    private fun getPossibleAnswers(): MutableList<String> {
        var answerOptions: MutableList<String> = mutableListOf()
        currentItem.rounds.forEach { pair ->
            answerOptions.addAll(pair.answers)
        }
        if (answerOptions.size > 1) answerOptions = answerOptions.distinct().toMutableList()
        return answerOptions
    }


    private fun getCorrectAnswers(): List<String> {
        return currentItem.rounds[questionIdx].answers
    }


    private fun getQuestion(): String {
        return currentItem.rounds[questionIdx].question
    }

    override fun scorePercentage(): Int {
        return (score * 100) / clickCounter
    }
}


class EyesightDifferViewModelFactory(
    private val repo: EyesightDifferRepo,
    private val app: LogoApp,
    private val levelIndex: Int
) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightDifferViewModel::class.java)) {
            return EyesightDifferViewModel(repo, app, levelIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightDifferViewModel")
    }
}