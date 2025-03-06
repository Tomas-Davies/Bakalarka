package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.states.ResultMessageState
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit


/**
 * A wrapper composable that provides *Result feedback* animation.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param viewModel RoundsViewModel providing answerResultState, which controls the result
 * indication.
 * @param contentAlignment Alignment to be applied on the content.
 * @param content The content of the Box
 */
@Composable
fun AnswerResultBox(
    modifier: Modifier = Modifier,
    viewModel: RoundsViewModel,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {

        content()

        val answerResultState by viewModel.resultMessageState.collectAsStateWithLifecycle()

        AnswerResultCard(
            modifier = Modifier.align(Alignment.Center),
            answerResultState = answerResultState
        )
        val width = with(LocalDensity.current) { maxWidth.toPx() }
        val height = with(LocalDensity.current) { maxHeight.toPx() }
        val party = Party(
            angle = 45,
            speed = 3f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 90,
            position = Position.Absolute(0f, 0f),
            timeToLive = 300L,
            fadeOutEnabled = true,
            size = listOf(Size.LARGE),
            emitter = Emitter(duration = 300L, TimeUnit.MILLISECONDS).max(50)
        )
        val parties = listOf(
            party,
            party.copy(
                angle = 135,
                position = Position.Absolute(width, 0f)
            ),
            party.copy(
                angle = 315,
                position = Position.Absolute(0f, height)
            ),
            party.copy(
                angle = 225,
                position = Position.Absolute(width, height)
            )
        )

        if (answerResultState.showMessage && answerResultState.correctAnswer) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                KonfettiView(
                    modifier = Modifier.fillMaxSize(),
                    parties = parties
                )
            }
        }
    }
}

/**
 * A Card displaying the Result
 *
 * @param modifier Modifier to be applied to the layout.
 * @param answerResultState State, which controls the animation.
 */
@Composable
private fun AnswerResultCard(
    modifier: Modifier = Modifier,
    answerResultState: ResultMessageState
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = answerResultState.showMessage,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth * 2 }),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth * 2 })
    ) {
        var chosenColor = Color.Red
        var txt = stringResource(id = R.string.wrong_label)
        if (answerResultState.correctAnswer) {
            chosenColor = Color.Green
            txt = stringResource(id = R.string.correct_label)
        }
        if (answerResultState.message.isNotEmpty()) txt = answerResultState.message

        ElevatedCard(
            modifier = Modifier.padding(15.dp),
            colors = CardColors(
                contentColor = Color.Black,
                containerColor = chosenColor,
                disabledContentColor = CardDefaults.cardColors().disabledContentColor,
                disabledContainerColor = CardDefaults.cardColors().disabledContainerColor
            )
        ) {
            Text(
                modifier = Modifier.padding(15.dp),
                text = txt,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}