package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.states.ResultMessageState
import com.example.bakalarkaapp.viewModels.RoundsViewModel


/**
 * Composable providing functionality of showing a [AnswerResultCard] or
 * [RoundCompletedDialog] on top of [content].
 *
 * @param modifier Modifier to be applied to the layout.
 * @param viewModel Provides states which control visibility of the [AnswerResultCard] and
 * [RoundCompletedDialog].
 * @param contentAlignment Alignment to be applied to the content of [Box].
 * @param onExit Called when *exit* button is clicked.
 * @param content A Composable displayed inside the [Box].
 */
@Composable
fun RoundsCompletedBox(
    modifier: Modifier = Modifier,
    viewModel: RoundsViewModel,
    contentAlignment: Alignment = Alignment.TopStart,
    onExit: () -> Unit,
    content: @Composable BoxScope.() -> Unit
){
    val roundCompleteDialogShow by viewModel.roundCompletedDialogShow.collectAsStateWithLifecycle()
    val answerResultState by viewModel.resultMessageState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentAlignment = contentAlignment
    ){
        content()

        AnswerResultCard(
            modifier = Modifier.align(Alignment.Center),
            answerResultState = answerResultState
        )

        if (roundCompleteDialogShow){
            RoundCompletedDialog(
                scorePercentage = viewModel.scorePercentage(),
                onContinue = { viewModel.onContinue() },
                onExit = { onExit() },
                continueBtnEnabled = viewModel.hasNextRound
            )
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
            colors = CardDefaults.cardColors().copy(
                contentColor = Color.Black,
                containerColor = chosenColor
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