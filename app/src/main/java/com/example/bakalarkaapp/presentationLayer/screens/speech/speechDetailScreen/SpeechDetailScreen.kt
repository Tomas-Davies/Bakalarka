package com.example.bakalarkaapp.presentationLayer.screens.speech.speechDetailScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.PlaySoundButton
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.theme.AppTheme

class SpeechDetailScreen: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val letterLabel = intent.getStringExtra("LETTER_LABEL") ?: ""
        val posLabel = intent.getStringExtra("POS_LABEL") ?: ""

        val app = application as LogoApp
        val viewModel: SpeechDetailViewModel by viewModels {
            SpeechDetailViewModelFactory(app, letterLabel, posLabel)
        }

        setContent {
            AppTheme(ThemeType.THEME_SPEECH.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SpeechDetailContent(viewModel, letterLabel, posLabel)
                }
            }
        }
    }

    @Composable
    private fun SpeechDetailContent(
        viewModel: SpeechDetailViewModel,
        letterLabel: String,
        posLabel: String
    ){
        val category = if (posLabel != "NONE") posLabel else letterLabel
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.category_speech) + " - " + category
        ){pdVal ->
            SpeechDetail(pdVal, viewModel)
        }
    }


    @Composable
    private fun SpeechDetail(
        pdVal: PaddingValues,
        viewModel: SpeechDetailViewModel
    ){
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp, pdVal.calculateTopPadding(), 15.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val drawableName = uiState.currentWord.imageName ?: ""
            val drawableId = viewModel.getDrawableId(drawableName)

            Column(
                modifier = Modifier.weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    Image(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxWidth(),
                        painter = painterResource(id = drawableId),
                        contentDescription = "image"
                    )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = uiState.currentWord.text ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                modifier = Modifier
                    .weight(0.2f)
                    .wrapContentHeight(),
                text = "${uiState.index + 1} / ${viewModel.count}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.scale(1.5f),
                    onClick = { viewModel.previous() },
                    enabled = !uiState.isOnFirstWord
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "button previous"
                    )
                }
                val soundName = uiState.currentWord.soundName ?: ""
                val soundId = viewModel.getSoundId(soundName)

                PlaySoundButton(
                    onClick = { viewModel.playSound(soundId) }
                )
                IconButton(
                    modifier = Modifier.scale(1.5f),
                    onClick = { viewModel.next() },
                    enabled = !uiState.isOnLastWord
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "button next"
                    )
                }
            }
        }
    }
}