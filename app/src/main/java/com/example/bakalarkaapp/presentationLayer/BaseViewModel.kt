package com.example.bakalarkaapp.presentationLayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.states.AnswerResultState
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: LogoApp): ViewModel() {
    private val appContext = application.applicationContext
    protected var roundIdx = 0
    protected var score = 0
    protected val _screenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()
    protected var isFirstCorrectAttempt = true
    protected var isFirstWrongAttempt = true
    var count = 0
    private var _answerResultState = MutableStateFlow(AnswerResultState())
    val answerResultState = _answerResultState.asStateFlow()
    protected var _buttonsEnabled = MutableStateFlow(true)
    val buttonsEnabled = _buttonsEnabled.asStateFlow()

    protected open fun nextRound(): Boolean {
        if (roundIdx + 1 < count) {
            roundIdx++
            return true
        } else {
            _screenState.value = ScreenState.Finished
            return false
        }
    }

    protected open fun doRestart(){}

    fun restart(){
        score = 0
        roundIdx = 0
        doRestart()
        _screenState.value = ScreenState.Running
    }

    open fun scorePercentage(): Int {
        return (score * 100) / count
    }

    protected fun resetAttemptFlags(){
        isFirstCorrectAttempt = true
        isFirstWrongAttempt = true
    }

    protected abstract fun updateData()

    private fun updateAnswerResultState(result: Boolean = false, show: Boolean = false, message: String = ""){
        _answerResultState.update { state ->
            state.copy(
                showResult = show,
                correctAnswer = result,
                message = message
            )
        }
    }

    protected fun showMessage(result: Boolean = answerResultState.value.correctAnswer, message: String = ""){
        viewModelScope.launch {
            updateAnswerResultState(show = true, result = result, message = message)
            delay(1000)
            updateAnswerResultState(show = false, result = result, message = message)
        }
    }


    fun playSound(soundId: Int) {
        viewModelScope.launch {
            val mediaPlayer = MediaPlayer.create(appContext, soundId)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
            }
        }
    }

    fun playResultSound(result: Boolean){
        viewModelScope.launch {
            if (result){
                playSound(R.raw.correct_answer)
            } else {
                playSound(R.raw.wrong_answer)
            }
        }
    }

    private fun getVibrator(): Vibrator {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            appContext.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        }

        return vibrator
    }

    fun vibrate(vibrationEffect: VibrationEffect){
        viewModelScope.launch {
            val vibrator = getVibrator()
            vibrator.cancel()
            vibrator.vibrate(vibrationEffect)
        }
    }

}
