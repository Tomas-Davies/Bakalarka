package com.example.logopadix.presentationLayer.screens.rythm.rythmSyllablesScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.logopadix.LogoApp
import com.example.logopadix.R
import com.example.logopadix.theme.ThemeType
import com.example.logopadix.presentationLayer.components.AsyncDataWrapper
import com.example.logopadix.presentationLayer.components.PlaySoundButton
import com.example.logopadix.presentationLayer.components.RoundsCompletedBox
import com.example.logopadix.presentationLayer.components.ScreenWrapper
import com.example.logopadix.presentationLayer.screens.levels.IImageLevel
import com.example.logopadix.theme.AppTheme


class RythmSyllabelsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repo = app.rythmSyllablesRepository
        val levelIndex = intent.getIntExtra(IImageLevel.TAG, 0)
        val viewModel: RythmSyllablesViewModel by viewModels {
            RythmSyllablesViewModelFactory(repo, app, levelIndex)
        }
        setContent {
            AppTheme(ThemeType.THEME_RYTHM.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RythmSyllableScreenContent(viewModel)
                }
            }
        }
    }


    @Composable
    private fun RythmSyllableScreenContent(viewModel: RythmSyllablesViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.rythm_menu_label_2)
        ) {
            AsyncDataWrapper(viewModel = viewModel) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                RoundsCompletedBox(
                    viewModel = viewModel,
                    onExit = { finish() }
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.rythm_syllables_label),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        val imageId = viewModel.getDrawableId(uiState.imageName)
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(3f),
                            painter = painterResource(id = imageId),
                            contentDescription = ""
                        )
                        HorizontalDivider()
                        val soundId = viewModel.getSoundId(uiState.soundName)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            PlaySoundButton {
                                viewModel.playSound(soundId)
                            }
                        }

                        SyllablesRow(
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel
                        )

                        val enabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = { viewModel.onBtnClick() },
                                enabled = enabled
                            ) {
                                Text(text = stringResource(id = R.string.done_btn_label))
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun SyllablesRow(modifier: Modifier, viewModel: RythmSyllablesViewModel) {
        val buttonsStates by viewModel.buttonStates.collectAsStateWithLifecycle()
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            buttonsStates.forEachIndexed { idx, selected ->
                SyllableIndicator(
                    modifier = Modifier.weight(1f),
                    idx = idx,
                    viewModel = viewModel,
                    selected = selected
                )
            }
        }
    }

    @Composable
    private fun SyllableIndicator(
        modifier: Modifier,
        idx: Int,
        viewModel: RythmSyllablesViewModel,
        selected: Boolean
    ) {

        val color =
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        val buttonColors = ButtonDefaults.buttonColors(containerColor = color)
        val border = if (selected)
            BorderStroke(3.dp, MaterialTheme.colorScheme.surfaceDim)
        else BorderStroke(0.dp, MaterialTheme.colorScheme.surfaceDim)

        OutlinedButton(
            modifier = modifier
                .padding(8.dp)
                .aspectRatio(1f),
            onClick = {
                viewModel.onItemClick(idx)
            },
            shape = RoundedCornerShape(100),
            colors = buttonColors,
            border = border
        ) {}
    }
}