package com.example.bakalarkaapp.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * Interface defining the different types of answers for validation.
 */
sealed interface IValidationAnswer {
    data class StringAnswer(val value: String): IValidationAnswer
    data class BooleanAnswer(val value: Boolean): IValidationAnswer
}


/**
 * Abstract base ViewModel for rounds-based activities that require answer validation.
 *
 * It manages:
 * - Answer validation.
 * - Score updates.
 * - Playing an appropriate sound.
 * - Showing result messages.
 *
 * @param app The application instance that provides the application context.
 */
abstract class ValidatableRoundViewModel(app: LogoApp): RoundsViewModel(app) {

    /**
     * Validates the user's answer and performs the appropriate actions based on correctness.
     *
     * @param answer The answer provided by the user, wrapped in an IValidationAnswer type.
     * @return true if the answer is correct, false otherwise.
     */
    fun validateAnswer(answer: IValidationAnswer?): Boolean {
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

    /**
     * Defines the validation condition for determining if an answer is correct.
     * Must be implemented by subclasses with its specific logic.
     *
     * @param answer The answer to validate.
     * @return true if the answer is correct, false otherwise.
     */
    abstract fun validationCond(answer: IValidationAnswer?): Boolean


    protected open fun newData(){
        if (nextRound()) updateData()
    }

    protected open fun playOnCorrectSound(){
        playResultSound(result = true)
    }

    protected open fun playOnWrongSound(){
        playResultSound(result = false)
    }

    protected open fun beforeNewData(){
        _buttonsEnabled.update { false }
    }

    protected open fun afterNewData(){
        _buttonsEnabled.update { true }
    }

    protected open suspend fun messageShowCorrect(){
        showMessage(result = true)
        delay(1500)
    }

    protected open fun messageShowWrong(){
        showMessage(result = false)
    }
}