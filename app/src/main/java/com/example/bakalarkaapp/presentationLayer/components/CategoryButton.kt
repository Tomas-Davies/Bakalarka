package com.example.bakalarkaapp.presentationLayer.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.R

@Composable
fun CategoryButton(
    modifier: Modifier,
    label: String,
    labelLong: String,
    popUpHeading: String,
    popUpContent: String,
    imgId: Int,
    onClick: () -> Unit,
    bgColorPrimary: Color,
    bgColorSecondary: Color,
    textColor: Color
){
    ElevatedCard(
        shape = RoundedCornerShape(25.dp),
        onClick = { onClick() },
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardColors(
            contentColor = CardDefaults.cardColors().contentColor,
            containerColor = bgColorPrimary,
            disabledContentColor = CardDefaults.cardColors().disabledContentColor,
            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor
        )
    ) {
        CardContent(
            label = label,
            labelLong = labelLong,
            popUpHeading = popUpHeading,
            popUpContent = popUpContent,
            imgId = imgId,
            showMoreLabelColor = bgColorSecondary,
            textColor = textColor
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
    imgId: Int,
    showMoreLabelColor: Color,
    textColor: Color
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
            modifier = Modifier.weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = labelLong,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                color = textColor,
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            ShowMoreLabel(
                bgColor = showMoreLabelColor,
                textColor = textColor
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Top)
        ) {
            IconButton(onClick = { showPopUp = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "button detail info",
                    tint = textColor
                )
            }
        }

        Image(
            modifier = Modifier
                .weight(2f)
                .scale(0.8f),
            painter = painterResource(id = imgId),
            contentDescription = "decor image")
    }
}


@Composable
fun ShowMoreLabel(bgColor: Color, textColor: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(bgColor)
            .padding(8.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.label_show),
            color = textColor,
            fontSize = 12.sp
        )
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "play icon",
            tint = textColor
        )
    }
}