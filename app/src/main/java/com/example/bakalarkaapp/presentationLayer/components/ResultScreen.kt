package com.example.bakalarkaapp.presentationLayer.components

import android.app.Activity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.example.bakalarkaapp.R

@Composable
fun ResultScreen(
    scorePercentage: Int,
    onRestartBtnClick: () -> Unit
) {
    val ctx = LocalContext.current
    var medalLimit by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val medalDrawable = when {
            medalLimit < 33 -> R.drawable.bronze_medal
            medalLimit < 66 -> R.drawable.silver_medal
            else -> R.drawable.gold_medal
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            painter = painterResource(id = medalDrawable),
            contentDescription = "medal"
        )

        Spacer(modifier = Modifier.weight(0.1f))
        ResultProgressBar(
            indicatorProgress = scorePercentage.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            updateMedal = { progressAnimation ->
                medalLimit = when {
                    progressAnimation > 0.66F -> 66
                    progressAnimation > 0.33F -> 33
                    else -> 0
                }
            }
        )
        Spacer(modifier = Modifier.weight(0.3f))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val label = when {
                    scorePercentage < 33 -> stringResource(id = R.string.eyesight_comparison_result_1)
                    scorePercentage < 66 -> stringResource(id = R.string.eyesight_comparison_result_2)
                    scorePercentage < 85 -> stringResource(id = R.string.eyesight_comparison_result_3)
                    else -> stringResource(id = R.string.eyesight_comparison_result_4)
                }

                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    label = stringResource(id = R.string.label_again),
                    iconId = Icons.Filled.Refresh,
                    onClick = { onRestartBtnClick() }
                )
                IconButton(
                    label = stringResource(id = R.string.label_leave),
                    iconId = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = { (ctx as Activity).finish() }
                )
            }
        }
    }
}


@Composable
fun ResultProgressBar(
    indicatorProgress: Float,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    updateMedal: (progressAnimation: Float) -> Unit
) {
    var progress by remember { mutableFloatStateOf(0F) }
    val animDuration = 1700
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing),
        label = ""
    )

    val label = stringResource(id = R.string.correct_label)
    Text(
        text = "$label: ${(progressAnimation * 100).toInt()}%",
        style = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
    LinearProgressIndicator(
        progress = { progressAnimation },
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(12.dp)
    )
    LaunchedEffect(lifecycleOwner) {
        progress = indicatorProgress / 100
    }

    updateMedal(progressAnimation)
}