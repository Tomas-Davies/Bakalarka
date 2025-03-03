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
     * Must be implemented by subclasses with game-specific logic.
     *
     * @param answer The answer to validate.
     * @return true if the answer is correct, false otherwise.
     */
    abstract fun validationCond(answer: IValidationAnswer?): Boolean


    /**
     * Loads new data for the next round if available.
     * Can be overridden by subclasses to customize round progression.
     */
    protected open fun newData(){
        if (nextRound()) updateData()
    }

    /**
     * Plays the sound for a correct answer.
     * Can be overridden to customize sound behavior.
     */
    protected open fun playOnCorrectSound(){
        playResultSound(result = true)
    }

    /**
     * Plays the sound for a wrong answer.
     * Can be overridden to customize sound behavior.
     */
    protected open fun playOnWrongSound(){
        playResultSound(result = false)
    }

    /**
     * Performs actions before loading new data, such as disabling buttons.
     * Can be overridden to customize pre-load behavior.
     */
    protected open fun beforeNewData(){
        _buttonsEnabled.update { false }
    }

    /**
     * Performs actions after loading new data, such as re-enabling buttons.
     * Can be overridden to customize post-load behavior.
     */
    protected open fun afterNewData(){
        _buttonsEnabled.update { true }
    }

    /**
     * Shows the correct answer message and adds a delay before proceeding.
     * Can be overridden to customize messaging behavior.
     */
    protected open suspend fun messageShowCorrect(){
        showMessage(result = true)
        delay(1500)
    }

    /**
     * Shows the wrong answer message.
     * Can be overridden to customize messaging behavior.
     */
    protected open fun messageShowWrong(){
        showMessage(result = false)
    }
}