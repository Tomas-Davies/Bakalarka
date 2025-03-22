package com.example.logopadix.presentationLayer.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Composable displaying Linear Timer Indicator.
 *
 * @param msDuration Duration of the *timer* in milliseconds.
 * @param onFinish Called when *timer* finishes.
 * @param restartTrigger Updating this value triggers *timer* to restart.
 */
@Composable
fun LinearTimerIndicator(
    msDuration: Int,
    onFinish: () -> Unit,
    restartTrigger: Int,
) {
    val progressAnim = remember { Animatable(100f) }

    LaunchedEffect(restartTrigger) {
        progressAnim.snapTo(100f)
        progressAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = msDuration,
                easing = LinearEasing
            )
        )
    }
    LaunchedEffect(progressAnim.value) {
        if (progressAnim.value == 0f) {
            onFinish()
        }
    }
    LinearProgressIndicator(
        progress = { progressAnim.value / 100f },
        modifier = Modifier
            .fillMaxWidth()
            .height(15.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}