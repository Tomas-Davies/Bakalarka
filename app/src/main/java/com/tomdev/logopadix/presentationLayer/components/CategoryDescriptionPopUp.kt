package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tomdev.logopadix.R

/**
 * Composable representing a dialog showing text information.
 *
 * @param onExit Called when *exit* button is clicked.
 * @param popUpHeading Heading of the dialog.
 * @param popUpBody Body of the dialog.
 */
@Composable
fun CategoryDetailDialog(
    onExit: () -> Unit,
    popUpHeading: String,
    popUpBody: String,
    ageRec: String
){
    AlertDialog(
        onDismissRequest = { onExit() },
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "info icon") },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = popUpHeading)
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.padding(0.dp, 8.dp))
                Text(text = ageRec)
                HorizontalDivider(modifier = Modifier.padding(0.dp, 8.dp))
            }
        }
        ,
        text = { Text(text = popUpBody) },
        confirmButton = {  },
        dismissButton = {
            Button(onClick = { onExit() }) {
                Text(
                    text = stringResource(id = R.string.pop_up_dismiss_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}