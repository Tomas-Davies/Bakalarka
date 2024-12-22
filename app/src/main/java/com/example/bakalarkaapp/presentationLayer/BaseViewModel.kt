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
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: LogoApp): ViewModel() {
    val appContext = application.applicationContext
    protected var roundIdx = 0
    protected var score = 0
    protected val _screenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()
    protected var isFirstCorrectAttempt = true
    protected var isFirstWrongAttempt = true
    var count = 0



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
        val correctCount = score
        return (correctCount * 100) / count
    }

    protected fun resetAttemptFlags(){
        isFirstCorrectAttempt = true
        isFirstWrongAttempt = true
    }

    protected abstract fun updateData()


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
