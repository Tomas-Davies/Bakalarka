package com.example.logopadix.presentationLayer.components


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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logopadix.R


/**
 * Composable representing button used in Category Menu.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param label Main label of the button.
 * @param labelLong Secondary label with longer text.
 * @param popUpHeading Heading of [CategoryDetailDialog].
 * @param popUpContent Content of [CategoryDetailDialog].
 * @param imageId Drawable resource ID.
 * @param onClick Called when this button is clicked
 */
@Composable
fun CategoryButton(
    modifier: Modifier = Modifier,
    label: String,
    labelLong: String,
    popUpHeading: String,
    popUpContent: String,
    imageId: Int,
    onClick: () -> Unit
){
    val colors = CardDefaults.cardColors().copy(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    CustomCard(
        onClick = { onClick() },
        modifier = modifier,
        colors = colors,
        shape = RoundedCornerShape(25.dp)
    ) {
        CategoryContent(
            label = label,
            labelLong = labelLong,
            popUpHeading = popUpHeading,
            popUpContent = popUpContent,
            imageId = imageId
        )
    }
    Spacer(modifier = Modifier.height(15.dp))
}


@Composable
private fun CategoryContent(
    label:String,
    labelLong: String,
    popUpHeading: String,
    popUpContent: String,
    imageId: Int
){
    var showPopUp by remember { mutableStateOf(false) }
    if (showPopUp){
        CategoryDetailDialog(
            popUpHeading = popUpHeading,
            popUpBody = popUpContent,
            onExit = { showPopUp = false }
        )
    }
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
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = label,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
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
                .sizeIn(maxHeight = 115.dp),
            painter = painterResource(id = imageId),
            contentDescription = "decor image")
    }
}


@Composable
fun ShowMoreLabel() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(25.dp))
            .padding(8.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.show_label),
            style = MaterialTheme.typography.labelMedium
        )
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "play icon",
        )
    }
}