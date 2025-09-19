package com.tomdev.logopadix.presentationLayer.components


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: CardColors = CardDefaults.cardColors(),
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    outlineSelectedColor: Color = MaterialTheme.colorScheme.outlineVariant,
    content: @Composable ColumnScope.() -> Unit
){
    var pressed by remember { mutableStateOf(false) }
    val currentOutlineColor = if (pressed) outlineSelectedColor else outlineColor

    Surface(
        modifier = modifier,
        shape = shape,
        color = if (enabled) currentOutlineColor else currentOutlineColor.copy(alpha = 0.38f)
    ) {
        Surface(
            modifier = Modifier
                .padding(
                    start = 1.dp,
                    top = 1.dp,
                    end = 1.dp,
                    bottom = 4.dp
                )
                .pointerInput(enabled) {
                    detectTapGestures(
                        onPress = {
                            if (enabled) {
                                pressed = true
                                val released = tryAwaitRelease()
                                if (released) onClick()
                                pressed = false
                            }
                        }
                    )
                },
            shape = shape,
            color = if (enabled) colors.containerColor else colors.disabledContainerColor,
            contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
        ) {
            Column(content = content)
        }
    }
}


@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    shape: Shape = CardDefaults.shape,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    content: @Composable ColumnScope.() -> Unit
){
    Surface(
        modifier = modifier,
        shape = shape,
        color = outlineColor,
        contentColor = colors.contentColor
    ) {
        Surface(
            modifier = Modifier
                .padding(
                    start = 1.dp,
                    top = 1.dp,
                    end = 1.dp,
                    bottom = 4.dp
                ),
            shape = shape,
            color = colors.containerColor,
            contentColor = colors.contentColor
        ) {
            Column(content = content)
        }
    }
}