package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun ImageCard(
    drawable: Int,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier
){
    Card(
        modifier = modifier.padding(9.dp),
        enabled = enabled,
        onClick = { onClick() }
    ) {
        Image(
            modifier = imageModifier.padding(9.dp),
            painter = painterResource(id = drawable),
            contentDescription = ""
        )
    }
}