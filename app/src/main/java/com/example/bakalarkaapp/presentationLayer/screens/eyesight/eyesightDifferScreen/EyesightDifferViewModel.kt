package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.ValidatableRoundViewModel
import com.example.bakalarkaapp.dataLayer.models.Round
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class EyesightDifferUiState(
    val imageName: String,
    val answers: List<String>,
    val correctAnswers: List<String>,
    val question: String,
    val questionNumber: Int,
    val count: Int
)

class EyesightDifferViewModel(
    app: LogoApp, private val levelIndex: Int
) : ValidatableRoundViewModel(app) {
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
            imageName = currentItem.imageName,
            answers = getPossibleAnswers(),
            correctAnswers = getCorrectAnswers(),
            question = getQuestion(),
            questionNumber = questionNumber,
            count = countFromLevel
        )
    )
    val uiState = _uiState.asStateFlow()

    override fun validationCond(answer: IValidationAnswer): Boolean {
        if (answer is IValidationAnswer.StringAnswer) return (answer.value in getCorrectAnswers())
        throw IllegalArgumentException("$this expects answer of type String")
    }

    override fun newData() {
        nextQuestion()
    }

    override fun afterNewData() {}

    override fun doRestart() {
        questionIdx = 0
        currentItem = data[roundIdx]
        questionNumber = 1
        completeUiStateUpdate()
    }

    private fun nextQuestion() {
        val questions = getQuestions()
        if (questionIdx < questions.size - 1) {
            updateData()
        } else {
            nextRound()
        }
        _buttonsEnabled.update { true }
    }

    override fun updateData() {
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

    override fun nextRound(): Boolean {
        questionIdx = 0
        if (roundIdx < data.size - 1) {
            questionNumber++
            roundIdx++
            currentItem = data[roundIdx]
            completeUiStateUpdate()
            return true
        } else {
            _screenState.value = ScreenState.FINISHED
            return false
        }
    }

    private fun completeUiStateUpdate() {
        _uiState.update { currentState ->
            currentState.copy(
                imageName = currentItem.imageName,
                answers = getPossibleAnswers(),
                correctAnswers = getCorrectAnswers(),
                question = getQuestion(),
                questionNumber = questionNumber
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

    private fun getQuestions(): List<Round> {
        return currentItem.rounds
    }

    private fun getQuestion(): String {
        return currentItem.rounds[questionIdx].question
    }

    private fun getTotalQuestionsCount() {
        data.indices.forEach { setIdx ->
            data[setIdx].rounds.forEach { _ ->
                count++
                if (setIdx >= levelIndex) countFromLevel++
            }
        }
    }

    override fun scorePercentage(): Int {
        return (score * 100) / countFromLevel
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