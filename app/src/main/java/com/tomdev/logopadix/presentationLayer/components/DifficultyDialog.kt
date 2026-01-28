package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DifficultyDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    setIsVisible: (visible: Boolean) -> Unit,
    selectedExcerciseLabel: Int,
    selectedExcerciseImage: Int,
    difficultyHeading: String,
    easyLabel: String,
    mediumLabel: String,
    hardLabel: String,
    easyColor: Int,
    mediumColor: Int,
    hardColor: Int,
    onEasyClick: () -> Unit,
    onMediumClick: () -> Unit,
    onHardClick: () -> Unit,
    easyPainter: Painter,
    mediumPainter: Painter,
    hardPainter: Painter
){
    if (isVisible){
        BaseCustomDialog(
            modifier = modifier,
            onExit = {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(selectedExcerciseLabel),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    DeleteButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { setIsVisible(false) }
                    )
                }
                Image(
                    painter = painterResource(selectedExcerciseImage),
                    contentDescription = "decor image"
                )
                Text(
                    text = "$difficultyHeading:",
                    style = MaterialTheme.typography.headlineMedium
                )
                HorizontalDivider()
                DifficultyCard(
                    difficultyLabel = easyLabel,
                    painter = easyPainter,
                    onCardClick = {
                        onEasyClick()
                        setIsVisible(false)
                    },
                    colors = CardDefaults.cardColors().copy(containerColor = colorResource(easyColor)),
                    outlineColor = Color.DarkGray,
                    outlineSelectedColor = Color.Gray
                )
                DifficultyCard(
                    difficultyLabel = mediumLabel,
                    painter = mediumPainter,
                    onCardClick = {
                        onMediumClick()
                        setIsVisible(false)
                    },
                    colors = CardDefaults.cardColors().copy(containerColor = colorResource(mediumColor)),
                    outlineColor = Color.DarkGray,
                    outlineSelectedColor = Color.Gray
                )
                DifficultyCard(
                    difficultyLabel = hardLabel,
                    painter = hardPainter,
                    onCardClick = {
                        onHardClick()
                        setIsVisible(false)
                    },
                    colors = CardDefaults.cardColors().copy(containerColor = colorResource(hardColor)),
                    outlineColor = Color.DarkGray,
                    outlineSelectedColor = Color.Gray
                )
            }
        }
    }
}


@Composable
private fun DifficultyCard(
    difficultyLabel: String,
    painter: Painter,
    onCardClick: () -> Unit,
    colors: CardColors,
    outlineColor: Color,
    outlineSelectedColor: Color
) {
    CustomCard(
        modifier = Modifier
            .sizeIn(maxHeight = 100.dp)
            .fillMaxWidth(),
        onClick = { onCardClick() },
        colors = colors,
        outlineColor = outlineColor,
        outlineSelectedColor = outlineSelectedColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(18.dp),
                text = difficultyLabel,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painter,
                contentScale = ContentScale.Fit,
                contentDescription = "decor image"
            )
        }

    }
}