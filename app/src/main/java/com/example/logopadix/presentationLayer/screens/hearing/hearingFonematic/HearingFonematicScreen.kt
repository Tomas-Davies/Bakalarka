package com.example.logopadix.presentationLayer.screens.hearing.hearingFonematic

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.logopadix.LogoApp
import com.example.logopadix.R
import com.example.logopadix.theme.ThemeType
import com.example.logopadix.presentationLayer.components.AsyncDataWrapper
import com.example.logopadix.presentationLayer.components.ImageCard
import com.example.logopadix.presentationLayer.components.PlaySoundButton
import com.example.logopadix.presentationLayer.components.RoundsCompletedBox
import com.example.logopadix.presentationLayer.components.ScreenWrapper
import com.example.logopadix.theme.AppTheme

class HearingFonematicScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repo = app.hearingFonematicRepository
        val viewModel: HearingFonematicViewModel by viewModels {
            HearingFonematicFactory(repo, app)
        }
        setContent {
            AppTheme(ThemeType.THEME_HEARING.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearingFonematicScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun HearingFonematicScreenContent(viewModel: HearingFonematicViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.hearing_menu_label_1)
        ) {
            AsyncDataWrapper(viewModel = viewModel) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
                ) {
                    HearingFonematicRunning(viewModel = viewModel)
                }
            }
        }
    }


    @Composable
    private fun HearingFonematicRunning(viewModel: HearingFonematicViewModel) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val enabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()
        val soundName = uiState.playedObject.soundName ?: ""
        val soundId = viewModel.getSoundId(soundName)

        RoundsCompletedBox(
            viewModel = viewModel,
            onExit = { finish() }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(0.5f),
                    text = stringResource(id = R.string.hearing_fonematic_label),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                AnimatedContent(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(0.7f),
                    targetState = uiState.objects,
                    label = ""
                ) { targetList ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        targetList.forEach { obj ->
                            val drawableName = obj.imageName ?: ""
                            val drawable = viewModel.getDrawableId(drawableName)
                            ImageCard(
                                modifier = Modifier
                                    .padding(9.dp)
                                    .weight(1f),
                                drawable = drawable,
                                enabled = enabled,
                                onClick = { viewModel.onCardClick(drawableName) }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ){
                    PlaySoundButton(
                        onClick = {
                            viewModel.playSound(soundId)
                            viewModel.enableButtons()
                        }
                    )
                }
            }
        }
    }
}