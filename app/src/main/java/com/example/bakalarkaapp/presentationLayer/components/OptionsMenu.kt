package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun OptionsMenu(
    modifier: Modifier = Modifier,
    activeIndex: Int = 0,
    onOptionClick: (activeIdx: Int) -> Unit,
    itemLabels: List<String>
){
    var activeIdx by remember { mutableIntStateOf(activeIndex) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        itemLabels.forEachIndexed { index, label ->
            OptionsItem(
                modifier = Modifier.weight(1f),
                idx = index,
                activeIdx = activeIdx,
                label = label,
                onOptionClick = { idx ->
                    activeIdx = idx
                    onOptionClick(idx)
                }
            )
        }
    }
}


@Composable
private fun OptionsItem(
    modifier: Modifier = Modifier,
    idx: Int,
    activeIdx: Int,
    label: String,
    onOptionClick: (activeIdx: Int) -> Unit
){
    val color: Color by animateColorAsState(
        targetValue = if (idx == activeIdx)
            MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "color animation"
    )

    Surface(
        modifier = modifier,
        color = color,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        onClick = { onOptionClick(idx) }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text(
                modifier = Modifier.padding(18.dp),
                text = label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}