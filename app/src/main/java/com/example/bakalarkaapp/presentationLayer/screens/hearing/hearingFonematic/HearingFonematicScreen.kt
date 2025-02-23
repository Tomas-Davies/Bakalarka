package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResultBox
import com.example.bakalarkaapp.presentationLayer.components.ImageCard
import com.example.bakalarkaapp.presentationLayer.components.PlaySoundButton
import com.example.bakalarkaapp.presentationLayer.components.RunningOrFinishedRoundScreen
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.theme.AppTheme

class HearingFonematicScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(ThemeType.THEME_HEARING) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
       //             color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as LogoApp
                    val viewModel: HearingFonematicViewModel by viewModels {
                        HearingFonematicFactory(app)
                    }
                    HearingFonematicScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun HearingFonematicScreenContent(viewModel: HearingFonematicViewModel) {
        ScreenWrapper(
            headerLabel = stringResource(id = R.string.hearing_menu_label_1)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
            ) {
                RunningOrFinishedRoundScreen(viewModel = viewModel) {
                    HearingFonematicRunning(viewModel = viewModel)
                }
            }
        }
    }


    @Composable
    private fun HearingFonematicRunning(viewModel: HearingFonematicViewModel) {
        val uiState = viewModel.uiState.collectAsState().value
        val enabled = viewModel.buttonsEnabled.collectAsState().value
        val soundName = uiState.playedObject.soundName ?: ""
        val soundId = viewModel.getSoundId(soundName)

        AnswerResultBox(viewModel = viewModel) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.hearing_fonematic_label),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                AnimatedContent(
                    modifier = Modifier
                        .weight(4f)
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
                            val drawableName = obj.imgName ?: ""
                            val drawable = viewModel.getDrawableId(drawableName)
                            ImageCard(
                                modifier = Modifier
                                    .padding(9.dp)
                                    .weight(1f),
                                drawable = drawable,
                                enabled = enabled,
                                onClick = {
                                    viewModel.validateAnswer(
                                        IValidationAnswer.StringAnswer(drawableName)
                                    )
                                }
                            )
                        }
                    }
                }
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