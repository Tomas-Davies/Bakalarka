package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    app: LogoApp, levelIndex: Int
) : RoundsViewModel(app) {
    init {
        roundIdx = levelIndex
    }

    private val differRepo = app.eyesightDifferRepository
    private val data = differRepo.data
    private var questionIdx = 0
    private var currentItem = data[roundIdx]
    private var _questionNumber = MutableStateFlow(1)
    val questionNumber = _questionNumber.asStateFlow()
    private var _questionCountInRound = MutableStateFlow(currentItem.rounds.size)
    val questionCountInRound = _questionCountInRound.asStateFlow()

    init {
        count = data.size
    }

    private val _uiState = MutableStateFlow(
        EyesightDifferUiState(
            imageName = currentItem.imageName,
            answers = getPossibleAnswers(),
            correctAnswers = getCorrectAnswers(),
            question = getQuestion()
        )
    )
    val uiState = _uiState.asStateFlow()
    override var roundSetSize = 2
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

class EyesightDifferViewModelFactory(private val app: LogoApp, private val levelIndex: Int) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightDifferViewModel::class.java)) {
            return EyesightDifferViewModel(app, levelIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightDifferViewModel")
    }
}