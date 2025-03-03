package com.example.bakalarkaapp.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.states.ResultMessageState
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Abstract base ViewModel that provides common functionality for rounds-based activities.
 *
 * It manages:
 * - Round progression.
 * - Score tracking.
 * - Game state (running, finished).
 * - Result messaging.
 * - Button enabling / disabling.
 *
 * @param app The application instance that provides the application context.
 */
abstract class RoundsViewModel(app: LogoApp) : BaseViewModel(app) {
    private val appContext = app.applicationContext
    protected var roundIdx = 0
    protected var score = 0
    protected val _screenState = MutableStateFlow(ScreenState.RUNNING)
    val screenState = _screenState.asStateFlow()
    var count = 0
    private var _resultMessageState = MutableStateFlow(ResultMessageState())
    val resultMessageState = _resultMessageState.asStateFlow()
    protected var _buttonsEnabled = MutableStateFlow(true)
    val buttonsEnabled = _buttonsEnabled.asStateFlow()

    protected abstract fun updateData()

    protected open fun nextRound(): Boolean {
        if (roundIdx + 1 < count) {
            roundIdx++
            return true
        } else {
            _screenState.value = ScreenState.FINISHED
            return false
        }
    }

    /**
     * Performs additional restart operations.
     * Can be overridden by subclasses to handle game-specific restart logic.
     */
    protected open fun doRestart() {}

    fun restart() {
        score = 0
        roundIdx = 0
        doRestart()
        _screenState.value = ScreenState.RUNNING
    }


    private fun updateAnswerResultState(
        result: Boolean = false,
        show: Boolean = false,
        message: String = ""
    ) {
        _resultMessageState.update { state ->
            state.copy(
                showMessage = show,
                correctAnswer = result,
                message = message
            )
        }
    }

    protected fun showMessage(
        result: Boolean = resultMessageState.value.correctAnswer,
        message: String = ""
    ) {
        viewModelScope.launch {
            updateAnswerResultState(result, true, message)
            delay(1000)
            updateAnswerResultState(result, false, message)
        }
    }

    fun playResultSound(result: Boolean) {
        if (result) {
            playSound(R.raw.correct_answer)
        } else {
            playSound(R.raw.wrong_answer)
        }
    }

    fun enableButtons() {
        _buttonsEnabled.update { true }
    }

    protected open fun scoreInc() {
        score++
    }

    protected open fun scoreDesc() {
        if (score >= 0) score--
    }

    open fun scorePercentage(): Int {
        return (score * 100) / count
    }

}
