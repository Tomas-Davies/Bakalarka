package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R

@Composable
fun PlaySoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.speech_500)),
    ) {
        Image(
            modifier = Modifier.scale(2f),
            painter = painterResource(id = R.drawable.sound_on_icon),
            contentDescription = "sound on icon"
        )
    }
}
