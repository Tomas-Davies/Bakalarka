package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.Round
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
    val questionNumber: Int
)

class EyesightDifferViewModel(app: LogoApp): BaseViewModel(app) {
    private val differRepo = app.eyesightDifferRepository
    private val data = differRepo.data
            .shuffled()
            .sortedBy { differItem -> differItem.rounds.size  }
    private var questionIdx = 0
    private var currentItem = data[roundIdx]

    private var questionNumber = 1

    private val _uiState = MutableStateFlow(
        EyesightDifferUiState(
            imageId = currentItem.imageId.value,
            answers = getPossibleAnswers(),
            correctAnswers =  getCorrectAnswers(),
            question = getQuestion(),
            questionNumber = questionNumber

        )
    )
    val uiState: StateFlow<EyesightDifferUiState> = _uiState.asStateFlow()
    init {
        count = getQuestionsCount()
    }


    fun validateAnswer(answer: String): Boolean {
        val correctAnswers = getCorrectAnswers()
        if (correctAnswers.contains(answer)){
            if (isFirstCorrectAttempt) {
                score++
                isFirstCorrectAttempt = false
            }
            nextQuestion()
            return true
        } else {
            if (isFirstWrongAttempt) {
                score--
                isFirstWrongAttempt = false
            }
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
                imageId = currentItem.imageId.value,
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

    private fun getQuestionsCount(): Int{
        var counter = 0
        for (set in data) {
            for (i in set.rounds){
                counter++
            }
        }
        return counter
    }
}

class EyesightDifferViewModelFactory(private val app: LogoApp): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightDifferViewModel::class.java)){
            return EyesightDifferViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightDifferViewModel")
    }
}