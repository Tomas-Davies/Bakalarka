package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

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
        val app = application as LogoApp
        val viewModel: HearingFonematicViewModel by viewModels {
            HearingFonematicFactory(app)
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
            title = stringResource(id = R.string.hearing_menu_label_1)
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
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val enabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()
        val soundName = uiState.playedObject.soundName ?: ""
        val soundId = viewModel.getSoundId(soundName)

        AnswerResultBox(viewModel = viewModel) {
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