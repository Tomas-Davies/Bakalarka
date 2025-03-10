package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    label: String,
    iconId: ImageVector,
    onClick: () -> Unit,
    style: TextStyle = MaterialTheme.typography.labelLarge,
){
    val cardColors = CardColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = CardDefaults.cardColors().contentColor,
        disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
        disabledContentColor = CardDefaults.cardColors().disabledContentColor
    )
    Card(
        modifier = modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(9.dp),
        colors = cardColors
    ) {
        Row(
            modifier = Modifier.padding(28.dp, 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconId,
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = label,
                style = style,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}