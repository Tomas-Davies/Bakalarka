package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.theme.typography

/**
 * Composable representing a custom dialog made to display any Composable content.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param heading Heading of the dialog.
 * @param onExit Called when *exit* button is clicked.
 * @param showHeading Boolean value controlling *heading* [Text] visibility.
 * @param showExitButton Boolean value controlling *exit* [Button] visibility.
 * @param content A Composable displayed in the dialog body.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    heading: String = "",
    onExit: () -> Unit,
    showHeading: Boolean = true,
    showExitButton: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
){
    BasicAlertDialog(
        modifier = Modifier.padding(top = 18.dp, bottom = 18.dp).then(modifier),
        onDismissRequest = { onExit() }
    ) {
        Surface(
            modifier = Modifier.border(
                3.dp,
                MaterialTheme.colorScheme.surfaceVariant,
                AlertDialogDefaults.shape
            ),
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column {
                if (showHeading){
                    Text(
                        modifier = Modifier
                            .padding(18.dp)
                            .align(Alignment.CenterHorizontally),
                        text = heading,
                        textAlign = TextAlign.Center,
                        style = typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }

                content()

                if (showExitButton){
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { onExit() }
                        ) {
                            Text(
                                text = stringResource(id = R.string.pop_up_dismiss_label),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}