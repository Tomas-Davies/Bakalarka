package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.viewModels.RoundsViewModel

@Composable
fun AnswerResultBox(
    modifier: Modifier = Modifier,
    viewModel: RoundsViewModel,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
){
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {

        content()

        AnswerResult(
            viewModel = viewModel,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
private fun AnswerResult(viewModel: RoundsViewModel, modifier: Modifier = Modifier){
    val answerResultState = viewModel.resultMessageState.collectAsState().value
    AnimatedVisibility(
        modifier = modifier,
        visible = answerResultState.showMessage,
        enter = slideInHorizontally(initialOffsetX = {fullWidth ->  -fullWidth * 2}),
        exit = slideOutHorizontally(targetOffsetX = {fullWidth ->  fullWidth * 2})
    ) {
        var chosenColor = Color.Red
        var txt = stringResource(id = R.string.wrong_label)
        if (answerResultState.correctAnswer) {
            chosenColor = Color.Green
            txt = stringResource(id = R.string.correct_label)
        }
        if (answerResultState.message != "") txt = answerResultState.message

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