package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bakalarkaapp.R

@Composable
fun DetailPopUp(
    onExit: () -> Unit,
    popUpHeading: String,
    popUpBody: String
){
    AlertDialog(
        onDismissRequest = { onExit() },
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "info icon") },
        title = { Text(text = popUpHeading) },
        text = { Text(text = popUpBody) },
        confirmButton = {  },
        dismissButton = {
            Button(onClick = { onExit() }) {
                Text(text = stringResource(id = R.string.pop_up_dismiss_label))
            }
        }
    )
}