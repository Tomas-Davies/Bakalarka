package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmRepeatScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.theme.AppTheme
import com.linc.audiowaveform.AudioWaveform

class RythmRepeatScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel: RythmRepeatViewModel by viewModels {
            RythmRepeatViewModelFactory(app)
        }

        setContent {
            AppTheme(ThemeType.THEME_RYTHM.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RythmRepeatScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun RythmRepeatScreenContent(viewModel: RythmRepeatViewModel){
        val rythmResources = viewModel.sounds.collectAsStateWithLifecycle().value
        val currentlyPlaying = viewModel.currentlyPlayngIdx.collectAsStateWithLifecycle().value

        ScreenWrapper(
            title = stringResource(id = R.string.rythm_repeat_heading)
        ){ pdVal ->

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(18.dp, pdVal.calculateTopPadding(), 18.dp, 18.dp),
                columns = GridCells.Adaptive(300.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                itemsIndexed(rythmResources){idx, rythmResource ->
                    SoundCard(
                        soundId = rythmResource.soundId,
                        amplitudes = rythmResource.amplitudes,
                        viewModel = viewModel,
                        number = idx + 1,
                        currentlyPlayingIdx = currentlyPlaying
                    )
                }
            }
        }
    }


    @Composable
    private fun SoundCard(
        soundId: Int,
        amplitudes: List<Int>,
        viewModel: RythmRepeatViewModel,
        number: Int,
        currentlyPlayingIdx: Int
    ){
        val isPlaying = currentlyPlayingIdx == number

        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            val cardColors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = CardDefaults.cardColors().contentColor,
                disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
                disabledContentColor = CardDefaults.cardColors().disabledContentColor,
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(25.dp)),
                colors = cardColors,
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 7.dp),
                    text = stringResource(id = R.string.rythm_sound_card_label) + " $number",
                    fontWeight = FontWeight.SemiBold,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(9.dp),
                    thickness = 2.dp,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.padding(9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imageModifier = Modifier.weight(0.5f)

                    if (isPlaying){
                        Image(
                            modifier = imageModifier,
                            painter = painterResource(id = R.drawable.stop_icon),
                            contentDescription = "play sound icon"
                        )
                    } else {
                        Image(
                            modifier = imageModifier,
                            painter = painterResource(id = R.drawable.play_icon),
                            contentDescription = "stop sound icon"
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.5f))
                    AudioWaveform(
                        modifier = Modifier.weight(3f),
                        amplitudes = amplitudes,
                        spikeRadius = 8.dp
                    ){}
                }
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        viewModel.playSound(soundId, number)
                    }
            )
        }
    }
}