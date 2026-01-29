package com.tomdev.logopadix.presentationLayer.screens.hearing.hearingSynthesis

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.repositories.StickerType
import com.tomdev.logopadix.presentationLayer.DifficultyType
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.ImageCard
import com.tomdev.logopadix.presentationLayer.components.PlaySoundButton
import com.tomdev.logopadix.presentationLayer.components.RoundsCompletedBox
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.services.DayStreakService
import com.tomdev.logopadix.theme.AppTheme

class HearingSynthesisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as com.tomdev.logopadix.LogoApp
        val repo = app.hearingSynthesisRepository
        val diff = intent.getStringExtra(DifficultyType.TAG) ?: ""
        val streakService = DayStreakService(app.applicationContext)

        val viewModel: HearingSynthesisViewModel by viewModels {
            HearingSynthesisViewModelFactory(repo, app, diff, streakService)
        }
        setContent {
            AppTheme(ThemeType.THEME_HEARING.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearingSynthScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun HearingSynthScreenContent(viewModel: HearingSynthesisViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            showPlaySoundIcon = true,
            soundAssignmentId = R.raw.play_recording_and_choose_picture,
            viewModel = viewModel,
            title = stringResource(id = R.string.hearing_menu_label_3)
        ) {
            AsyncDataWrapper(viewModel = viewModel) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp, it.calculateTopPadding(), 18.dp, it.calculateBottomPadding()+18.dp)
                ) {
                    HearingSynthRunning(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun HearingSynthRunning(viewModel: HearingSynthesisViewModel) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        RoundsCompletedBox(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            contentAlignment = Alignment.Center,
            onExit = { finish() },
            stickerId = StickerType.HEARING_SYNTHESIS.id
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.hearing_fonematic_label),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f),
                    contentAlignment = Alignment.Center
                ){
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth(),
                        columns = GridCells.Adaptive(150.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(uiState.roundObjects) { obj ->
                            val drawableName = obj.imageName ?: ""
                            val enabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()
                            val drawable = viewModel.getDrawableId(drawableName)
                            ImageCard(
                                modifier = Modifier.aspectRatio(1f),
                                drawable = drawable,
                                enabled = enabled,
                                onClick = {
                                    viewModel.onCardClick(drawableName)
                                }
                            )
                        }
                    }
                }

                val soundName = uiState.initObject.soundName ?: ""
                val soundId = viewModel.getSoundId(soundName)
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