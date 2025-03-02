package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.viewModels.RoundsViewModel


/**
 * A wrapper composable that calls *runningContent* or *onFinish* composable based on *screenState* from *RoundsViewModel*
 *
 * @param viewModel RoundsViewModel providing *screenState* and functionality for [ResultScreen].
 * @param onFinish Gets called when *screenState* is set to [ScreenState.FINISHED]
 * @param runningContent Is displayed as long as the *screenState* is set to [ScreenState.RUNNING]
 */
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
    val screenState = viewModel.screenState.collectAsStateWithLifecycle().value
    when (screenState) {
        ScreenState.RUNNING -> runningContent()
        ScreenState.FINISHED -> onFinish()
    }
}