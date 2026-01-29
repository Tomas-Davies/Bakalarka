package com.tomdev.logopadix.presentationLayer.components


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.repositories.StickerUiModel
import com.tomdev.logopadix.viewModels.RoundsViewModel
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

/**
 * Composable showing statistics of how well did user do in a set of rounds.
 *
 * @param scorePercentage Percentage of correct answers.
 * @param onContinue Called when *continue* button is clicked.
 * @param onExit Called when *exit* button is clicked.
 * @param continueBtnEnabled Boolean value controlling if *continue* button is enabled.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RoundCompletedDialog(
    viewModel: RoundsViewModel,
    scorePercentage: Int,
    onContinue: () -> Unit,
    onExit: () -> Unit,
    stickerId: String,
    continueBtnEnabled: Boolean
) {
    CustomDialog(
        showExitButton = false,
        onExit = { }
    ) {
        val stickerState by viewModel.stickerStateFlow.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var progress by remember { mutableFloatStateOf(0f) }
            var medalLimit by remember { mutableIntStateOf(0) }

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                val party = Party(
                    angle = 0,
                    speed = 3f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    spread = 100,
                    timeToLive = 10_000L,
                    fadeOutEnabled = true,
                    size = listOf(Size.LARGE),
                    emitter = Emitter(duration = 1_000L, TimeUnit.MILLISECONDS).max(100)
                )
                val parties = listOf(
                    party,
                    party.copy(angle = 180)
                )
                KonfettiView(
                    modifier = Modifier.matchParentSize(),
                    parties = parties
                )
                val medalDrawable = when {
                    medalLimit < 33 -> R.drawable.bronze_medal
                    medalLimit < 66 -> R.drawable.silver_medal
                    else -> R.drawable.gold_medal
                }
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(maxHeight = 300.dp),
                    painter = painterResource(id = medalDrawable),
                    contentDescription = "medal"
                )
            }
            if (stickerState != null){
                HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp))
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(R.string.collected_sticker),
                    style = MaterialTheme.typography.titleMedium
                )
                CollectedStickerRow(
                    sticker = stickerState,
                    viewModel = viewModel
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp))
            Text(
                text = stringResource(id = R.string.accuracy_label) + ": ${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(18.dp))
            ResultProgressBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f),
                onBarValueChange = { progressValue ->
                    progress = progressValue
                    medalLimit = when {
                        progressValue > 0.66F -> 66
                        progressValue > 0.33F -> 33
                        else -> 0
                    }
                },
                targetProgress = scorePercentage.toFloat()
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp))
            Spacer(modifier = Modifier.height(9.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (stickerId.isNotEmpty() && scorePercentage == 100){
                            viewModel.setStickerUiModelFlow(stickerId)
                            viewModel.collectStickerPiece(stickerId)
                        }
                        onExit()
                    }
                ) {
                    Text(text = stringResource(id = R.string.leave_label))
                }
                Spacer(modifier = Modifier.weight(0.3f))
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = continueBtnEnabled,
                    onClick = {
                        if (stickerId.isNotEmpty() && scorePercentage == 100){
                            viewModel.setStickerUiModelFlow(stickerId)
                            viewModel.collectStickerPiece(stickerId)
                        }
                        onContinue()
                    }
                ) {
                    Text(text = stringResource(id = R.string.continue_label))
                }
            }
        }
    }
}


@Composable
fun CollectedStickerRow(sticker: StickerUiModel?, viewModel: RoundsViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(0.7f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sticker?.label ?: "",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.weight(1f))

        val drawableId = viewModel.getDrawableId(sticker?.imageName ?: "")
        Image(
            modifier = Modifier.sizeIn(maxHeight = 65.dp),
            painter = painterResource(drawableId),
            contentDescription = "collected sticker image"
        )

    }
}