package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAlertDialog(
    heading: String,
    onExit: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
){
    BasicAlertDialog(
        modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
        onDismissRequest = { onExit() }
    ) {
        val cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CardDefaults.cardColors().contentColor,
            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
            disabledContentColor = CardDefaults.cardColors().disabledContentColor
        )
        Card(
            colors = cardColors
        ) {
            Text(
                modifier = Modifier
                    .padding(9.dp)
                    .align(Alignment.CenterHorizontally),
                text = heading,
                textAlign = TextAlign.Center,
                style = typography.titleLarge
            )
            Spacer(modifier = Modifier.height(15.dp))

            content()

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
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