package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.dataLayer.EyesightDifferRepo
import com.example.bakalarkaapp.dataLayer.QaPair
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EyesightDifferUiState(
    val imageId: String,
    val answers: List<String>,
    val correctAnswers: List<String>,
    val question: String,
    val questionNumber: Int
)

class EyesightDifferViewModel(differRepo: EyesightDifferRepo): ViewModel() {
    private val data = differRepo.data
            .shuffled()
            .sortedBy { differItem -> differItem.questionAndAnswers.qaPairs.size  }
    private var round = 0
    private var questionIdx = 0
    private var currentItem = data[round]
    private var isFirstCorrectAttempt = true
    private var isFirstWrongAttempt = true
    private var score = 0
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private var questionNumber = 1

    private val _uiState = MutableStateFlow(
        EyesightDifferUiState(
            imageId = currentItem.imageId,
            answers = getPossibleAnswers(),
            correctAnswers =  getCorrectAnswers(),
            question = getQuestion(),
            questionNumber = questionNumber

        )
    )

    val uiState: StateFlow<EyesightDifferUiState> = _uiState.asStateFlow()
    var count = getQuestionsCount()
        private set

    fun validateAnswer(answer: String) {
        val correctAnswers = getCorrectAnswers()
        if (correctAnswers.contains(answer)){
            if (isFirstCorrectAttempt) {
                score++
                isFirstCorrectAttempt = false
            }
            nextQuestion()
        } else {
            if (isFirstWrongAttempt) {
                score--
                isFirstWrongAttempt = false
            }
        }
    }

    fun restart(){
        round = 0
        questionIdx = 0
        currentItem = data[round]
        questionNumber = 1
        completeUiStateUpdate()
        _screenState.value = ScreenState.Running
    }

    fun scorePercentage(): Int {
        val correctCount = score
        val questionCount = count
        return (correctCount * 100) / questionCount
    }

    private fun nextQuestion(){
        val questions = getQuestions()
        if (questionIdx < questions.size-1){
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
        } else {
            nextRound()
        }
    }

    private fun nextRound(){
        questionIdx = 0
        if (round < data.size-1){
            resetAttemptFlags()
            questionNumber++
            round++
            currentItem = data[round]
            completeUiStateUpdate()
        } else {
            _screenState.value = ScreenState.Finished
        }
    }

    private fun completeUiStateUpdate(){
        _uiState.update { currentState ->
            currentState.copy(
                imageId = currentItem.imageId,
                answers = getPossibleAnswers(),
                correctAnswers = getCorrectAnswers(),
                question = getQuestion(),
                questionNumber = questionNumber

            )
        }
    }

    private fun getPossibleAnswers(): MutableList<String> {
        var answerOptions: MutableList<String> = mutableListOf()
        for (pair in currentItem.questionAndAnswers.qaPairs){
            answerOptions.addAll(pair.answers.correctAnswer)
        }
        if (answerOptions.size > 1) answerOptions = answerOptions.distinct().toMutableList()
        return answerOptions
    }

    private fun getCorrectAnswers(): List<String> {
        return currentItem.questionAndAnswers.qaPairs[questionIdx].answers.correctAnswer
    }

    private fun getQuestions(): List<QaPair>{
        return currentItem.questionAndAnswers.qaPairs
    }

    private fun getQuestion(): String {
        return currentItem.questionAndAnswers.qaPairs[questionIdx].question
    }

    private fun getQuestionsCount(): Int{
        var counter = 0
        for (round in data) {
            for (i in round.questionAndAnswers.qaPairs){
                counter++
            }
        }
        return counter
    }

    private fun resetAttemptFlags(){
        isFirstCorrectAttempt = true
        isFirstWrongAttempt = true
    }

}

class EyesightDifferViewModelFactory(private val differRepo: EyesightDifferRepo): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightDifferViewModel::class.java)){
            return EyesightDifferViewModel(differRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightDifferViewModel")
    }
}