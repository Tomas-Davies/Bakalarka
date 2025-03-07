package com.example.bakalarkaapp.presentationLayer.components


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun ResultProgressBar(
    modifier: Modifier = Modifier,
    onBarValueChange: (progressValue: Float) -> Unit = { },
    indicatorProgress: Float
){
    var progress by remember { mutableFloatStateOf(0F) }
    val animDuration = 1_700
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing),
        label = ""
    )
    LinearProgressIndicator(
        progress = { progressAnimation },
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(12.dp)
    )
    LaunchedEffect(Unit) {
        progress = indicatorProgress / 100
    }
    onBarValueChange(progressAnimation)
}