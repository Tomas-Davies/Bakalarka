package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun CardStripe(
    modifier: Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Yellow.copy(alpha = 0.05f),
                        Color.Yellow.copy(alpha = 0.25f),
                        Color.Yellow.copy(alpha = 0.5f),
                        Color.Yellow.copy(alpha = 0.25f),
                        Color.Yellow.copy(alpha = 0.05f)
                    )
                )
            )

    )
}