package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseCustomDialog(
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    content: @Composable () -> Unit
){
    BasicAlertDialog(
        modifier = Modifier
            .padding(vertical = 18.dp)
            .then(modifier),
        onDismissRequest = { onExit() }
    ) {
        Surface(
            modifier = Modifier.border(
                3.dp,
                MaterialTheme.colorScheme.surfaceVariant,
                shape = AlertDialogDefaults.shape
            ),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            shape = AlertDialogDefaults.shape
        ) {
            content()
        }
    }
}