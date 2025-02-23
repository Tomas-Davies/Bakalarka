package com.example.bakalarkaapp

import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class ValidatableViewModel(app: LogoApp): RoundsViewModel(app) {
    fun validateAnswer(answer: IValidationAnswer): Boolean {
        if (validationCond(answer)){
            playOnCorrectSound()
            viewModelScope.launch {
                scoreInc()
                beforeNewData()
                messageShowCorrect()
                newData()
                afterNewData()
            }
            return true
        } else {
            playOnWrongSound()
            messageShowWrong()
            scoreDesc()
            return false
        }
    }

    abstract fun validationCond(answer: IValidationAnswer): Boolean

    protected open fun newData(){
        if (nextRound()) updateData()
    }

    protected open fun playOnCorrectSound(){
        playResultSound(result = true)
    }

    protected open fun playOnWrongSound(){
        playResultSound(result = false)
    }

    protected open suspend fun beforeNewData(){
        _buttonsEnabled.emit(false)
    }

    protected open suspend fun afterNewData(){
        _buttonsEnabled.emit(true)
    }

    protected open suspend fun messageShowCorrect(){
        showMessage(result = true)
        delay(1500)
    }

    protected open fun messageShowWrong(){
        showMessage(result = false)
    }
}