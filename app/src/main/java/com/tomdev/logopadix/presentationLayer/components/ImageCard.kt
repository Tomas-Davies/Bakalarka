package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    imageModifier: Modifier = Modifier.fillMaxSize()
){
    CustomCard(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClick() },
    ) {
        Image(
            modifier = Modifier.padding(9.dp).then(imageModifier),
            painter = painterResource(id = drawable),
            contentDescription = ""
        )
    }
}