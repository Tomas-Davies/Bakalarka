package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.viewModels.RoundsViewModel

@Composable
fun RunningOrFinishedRoundScreen(
    viewModel: RoundsViewModel,
    resultPercLabel: String = stringResource(id = R.string.correct_label),
    onFinish: @Composable () -> Unit = {
        ResultScreen(
            scorePercentage = viewModel.scorePercentage(),
            onRestartBtnClick = { viewModel.restart() },
            message = resultPercLabel
        )
    },
    runningContent: @Composable () -> Unit,
){
    val screenState = viewModel.screenState.collectAsState().value
    when (screenState) {
        ScreenState.RUNNING -> runningContent()
        ScreenState.FINISHED -> onFinish()
    }
}