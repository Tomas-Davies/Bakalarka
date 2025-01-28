package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.repositories.Round
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EyesightDifferUiState(
    val imageId: String,
    val answers: List<String>,
    val correctAnswers: List<String>,
    val question: String,
    val questionNumber: Int,
    val count: Int
)

class EyesightDifferViewModel(app: LogoApp, private val levelIndex: Int): BaseViewModel(app) {
    init {
        roundIdx = levelIndex
    }
    private val differRepo = app.eyesightDifferRepository
    private val data = differRepo.data
    private var questionIdx = 0
    private var currentItem = data[roundIdx]
    private var questionNumber = 1
    private var countFromLevel = 0
    init {
        getTotalQuestionsCount()
    }

    private val _uiState = MutableStateFlow(
        EyesightDifferUiState(
            imageId = currentItem.background.value,
            answers = getPossibleAnswers(),
            correctAnswers =  getCorrectAnswers(),
            question = getQuestion(),
            questionNumber = questionNumber,
            count = countFromLevel
        )
    )
    val uiState: StateFlow<EyesightDifferUiState> = _uiState.asStateFlow()


    fun validateAnswer(answer: String): Boolean {
        val correctAnswers = getCorrectAnswers()
        if (correctAnswers.contains(answer)){
            if (isFirstCorrectAttempt) {
                score++
                isFirstCorrectAttempt = false
            }
            showMessage(result = true)
            nextQuestion()
            return true
        } else {
            if (isFirstWrongAttempt) {
                score--
                isFirstWrongAttempt = false
            }
            showMessage(result = false)
            return false
        }
    }

    override fun doRestart(){
        questionIdx = 0
        currentItem = data[roundIdx]
        questionNumber = 1
        completeUiStateUpdate()
    }

    private fun nextQuestion(){
        val questions = getQuestions()
        viewModelScope.launch {
            delay(1500)
            if (questionIdx < questions.size-1){
                updateData()
            } else {
                nextRound()
            }
        }
    }

    override fun updateData() {
        resetAttemptFlags()
        questionNumber++
        questionIdx++
        _uiState.update { currentState ->
            currentState.copy(
                correctAnswers = getCorrectAnswers(),
                question = getQuestion(),
                questionNumber = questionNumber

            )
        }
    }

    override fun nextRound(): Boolean{
        questionIdx = 0
        if (roundIdx < data.size-1){
            resetAttemptFlags()
            questionNumber++
            roundIdx++
            currentItem = data[roundIdx]
            completeUiStateUpdate()
            return true
        } else {
            _screenState.value = ScreenState.Finished
            return false
        }
    }

    private fun completeUiStateUpdate(){
        _uiState.update { currentState ->
            currentState.copy(
                imageId = currentItem.background.value,
                answers = getPossibleAnswers(),
                correctAnswers = getCorrectAnswers(),
                question = getQuestion(),
                questionNumber = questionNumber

            )
        }
    }

    private fun getPossibleAnswers(): MutableList<String> {
        var answerOptions: MutableList<String> = mutableListOf()
        for (pair in currentItem.rounds){
            answerOptions.addAll(pair.answers.map { textValue ->  textValue.value })
        }
        if (answerOptions.size > 1) answerOptions = answerOptions.distinct().toMutableList()
        return answerOptions
    }

    private fun getCorrectAnswers(): List<String> {
        return currentItem.rounds[questionIdx].answers.map { textValue ->  textValue.value }
    }

    private fun getQuestions(): List<Round>{
        return currentItem.rounds
    }

    private fun getQuestion(): String {
        return currentItem.rounds[questionIdx].question.value
    }

    private fun getTotalQuestionsCount() {
        for (setIdx in data.indices) {
            for (i in data[setIdx].rounds){
                count++
                if (setIdx >= levelIndex) countFromLevel++
            }
        }
    }

    override fun scorePercentage(): Int {
        return (score * 100) / countFromLevel
    }
}

class EyesightDifferViewModelFactory(private val app: LogoApp, private val levelIndex: Int): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightDifferViewModel::class.java)){
            return EyesightDifferViewModel(app, levelIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightDifferViewModel")
    }
}