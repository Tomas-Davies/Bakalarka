package com.example.bakalarkaapp.presentationLayer.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R

@Composable
fun CategoryButton(
    modifier: Modifier = Modifier,
    label: String,
    labelLong: String,
    popUpHeading: String,
    popUpContent: String,
    imgId: Int,
    onClick: () -> Unit
){
    ElevatedCard(
        shape = RoundedCornerShape(25.dp),
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(25.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardColors(
            contentColor = CardDefaults.cardColors().contentColor,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = CardDefaults.cardColors().disabledContentColor,
            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor
        )
    ) {
        CardContent(
            label = label,
            labelLong = labelLong,
            popUpHeading = popUpHeading,
            popUpContent = popUpContent,
            imgId = imgId
        )
    }
    Spacer(modifier = Modifier.height(15.dp))
}


@Composable
private fun CardContent(
    label:String,
    labelLong: String,
    popUpHeading: String,
    popUpContent: String,
    imgId: Int
){
    var showPopUp by remember { mutableStateOf(false) }
    if (showPopUp){ DetailPopUp(popUpHeading = popUpHeading, popUpBody = popUpContent, onExit = { showPopUp = false }) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .padding(0.dp, 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = labelLong,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = label,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            ShowMoreLabel()
        }

            IconButton(
                modifier = Modifier.align(Alignment.Top),
                onClick = { showPopUp = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "button detail info",
                )
            }

        Image(
            modifier = Modifier
                .weight(2f)
                .scale(1.2f)
                .sizeIn(maxHeight = 125.dp),
            painter = painterResource(id = imgId),
            contentDescription = "decor image")
    }
}


@Composable
fun ShowMoreLabel() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.outline)
            .padding(8.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.label_show),
            style = MaterialTheme.typography.labelMedium
        )
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "play icon",
        )
    }
}