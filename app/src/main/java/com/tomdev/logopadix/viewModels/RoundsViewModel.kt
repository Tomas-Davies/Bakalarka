package com.tomdev.logopadix.viewModels

import android.os.VibrationEffect
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.repositories.StickerUiModel
import com.tomdev.logopadix.services.DayStreakService
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
 * - Result messaging.
 * - Button enabling / disabling.
 *
 * @param app The application instance that provides the application context.
 */
abstract class RoundsViewModel(
    app: com.tomdev.logopadix.LogoApp,
    private val streakService: DayStreakService,
) : BaseViewModel(app) {
    protected var roundIdx = 0
        set(value) {
            field = value
            updateHasNextRound()
        }
    protected var score = 0
    protected var clickCounter = 0

    var count = 0
        set(value) {
            field = value
            updateHasNextRound()
        }
    protected val stickerRepo = app.stickerRepo

    init {
        viewModelScope.launch {
            streakService.checkStreak()
        }
    }

    private var _resultMessageState = MutableStateFlow(ResultMessageState())
    val resultMessageState = _resultMessageState.asStateFlow()
    protected var _buttonsEnabled = MutableStateFlow(true)
    val buttonsEnabled = _buttonsEnabled.asStateFlow()
    private var _roundCompletedDialogShow = MutableStateFlow(false)
    val roundCompletedDialogShow = _roundCompletedDialogShow.asStateFlow()
    var hasNextRound = roundIdx + 1 < count
        private set

    protected abstract var roundSetSize: Int
    private var roundsCompletedCount = 0

    protected var _stickerStateFlow = MutableStateFlow<StickerUiModel?>(null)
        private set

    val stickerStateFlow = _stickerStateFlow.asStateFlow()

    protected abstract fun updateData()

    protected open fun nextRound(): Boolean {
        if (hasNextRound) {
            roundIdx++
            return true
        } else return false
    }

    protected fun roundSetCompletedCheck(): Boolean {
        return !hasNextRound || roundsCompletedCount == roundSetSize
    }


    fun onContinue(){
        roundsCompletedCount = 0
        clickCounter = 0
        score = 0
        _roundCompletedDialogShow.value = false
        doContinue()
    }

    protected open fun doContinue(){
        if (nextRound()) { updateData() }
    }

    protected fun showRoundSetDialog(){
        playSound(R.raw.celebration)
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        _roundCompletedDialogShow.value = true
    }

    private fun updateHasNextRound(){
        hasNextRound = roundIdx + 1 < count
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

    private suspend fun showMessage(
        result: Boolean = resultMessageState.value.correctAnswer,
        message: String = ""
    ) {
        updateAnswerResultState(result, true, message)
        delay(1000)
        updateAnswerResultState(result, false, message)
    }

    protected open fun playOnCorrectSound(){
        playResultSound(result = true)
    }

    protected open fun playOnWrongSound(){
        playResultSound(result = false)
    }

    protected open suspend fun showCorrectMessage(){
        showMessage(result = true)
    }

    protected open suspend fun showWrongMessage(){
        showMessage(result = false)
    }

    fun playResultSound(result: Boolean) {
        if (result) {
            playSound(R.raw.correct_answer)
        } else {
            playSound(R.raw.wrong_answer)
        }
    }

    fun enableButtons() {
        _buttonsEnabled.value = true
    }

    protected open fun disableButtons(){
        _buttonsEnabled.value = false
    }

    protected open fun scoreInc() {
        score++
    }

    protected fun clickedCounterInc(){
        clickCounter++
    }

    protected fun roundsCompletedInc(){
        roundsCompletedCount++
    }

    open fun scorePercentage(): Int {
        return if (clickCounter != 0) (score * 100) / clickCounter else 80
    }

    fun collectStickerPiece(stickerId: String){
        viewModelScope.launch {
            stickerRepo.collectedPiece(stickerId)
        }
    }

    fun setStickerUiModelFlow(stickerId: String) {
        viewModelScope.launch {
            _stickerStateFlow.value = stickerRepo.getStickerById(stickerId)
        }
    }

}

data class ResultMessageState (
    val showMessage: Boolean = false,
    var correctAnswer: Boolean = false,
    var message: String = ""
)