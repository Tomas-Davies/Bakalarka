package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tomdev.logopadix.R

@Composable
fun PlaySoundButton(
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(id = R.color.speech_500),
    onClick: () -> Unit,
){
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(15.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.sound_on_icon),
            contentDescription = "sound on icon"
        )
    }
}
