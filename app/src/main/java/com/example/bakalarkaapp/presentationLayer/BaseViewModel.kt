package com.example.bakalarkaapp.presentationLayer

import androidx.lifecycle.ViewModel
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel: ViewModel() {
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

    fun scorePercentage(): Int {
        val correctCount = score
        return (correctCount * 100) / count
    }

    protected fun resetAttemptFlags(){
        isFirstCorrectAttempt = true
        isFirstWrongAttempt = true
    }

    protected abstract fun updateData()
}