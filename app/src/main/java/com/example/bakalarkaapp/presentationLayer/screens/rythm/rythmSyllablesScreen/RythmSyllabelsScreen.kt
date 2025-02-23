package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmSyllablesScreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResultBox
import com.example.bakalarkaapp.presentationLayer.components.PlaySoundButton
import com.example.bakalarkaapp.presentationLayer.components.RunningOrFinishedRoundScreen
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.viewModels.IValidationAnswer


class RythmSyllabelsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val levelIndex = intent.getIntExtra("LEVEL_INDEX", 0)
        Log.e("LEVEL INDEX", "INDEX: $levelIndex")
        val viewModel: RythmSyllablesViewModel by viewModels {
            RythmSyllablesViewModelFactory(app, levelIndex)
        }
        setContent {
            AppTheme(ThemeType.THEME_RYTHM) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val ctx = LocalContext.current
                    RunningOrFinishedRoundScreen(
                        viewModel = viewModel,
                        onFinish = { (ctx as Activity).finish() }
                    ){
                        RythmSyllableScreenContent(viewModel)
                    }
                }
            }
        }
    }

    @Composable
    private fun RythmSyllableScreenContent(viewModel: RythmSyllablesViewModel) {
        ScreenWrapper(
            headerLabel = stringResource(id = R.string.rythm_menu_label_2)
        ) {
            val uiState = viewModel.uiState.collectAsState().value
            AnswerResultBox(viewModel = viewModel) {
                Column(
                    modifier = Modifier.padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val imageId = viewModel.getDrawableId(uiState.imgName)
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

                    val enabled = viewModel.buttonsEnabled.collectAsState().value
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { viewModel.validateAnswer(IValidationAnswer.BlankAnswer) },
                            enabled = enabled
                        ) {
                            Text(text = "Hotovo")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SyllablesRow(modifier: Modifier, viewModel: RythmSyllablesViewModel) {
        val buttonsStates = viewModel.buttonStates.collectAsState().value

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            buttonsStates.forEachIndexed { idx, enabled ->
                SyllableIndicator(
                    modifier = Modifier.weight(1f),
                    onClick = { },
                    idx = idx,
                    viewModel = viewModel,
                    enabled = enabled
                )
            }
        }
    }

    @Composable
    private fun SyllableIndicator(
        modifier: Modifier,
        onClick: () -> Unit,
        idx: Int,
        viewModel: RythmSyllablesViewModel,
        enabled: Boolean
    ) {

        val color =
            if (enabled) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
        val buttonColors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = ButtonDefaults.buttonColors().contentColor,
            disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor,
            disabledContentColor = ButtonDefaults.buttonColors().disabledContentColor
        )

        val border = if (enabled)
            BorderStroke(0.dp, MaterialTheme.colorScheme.surfaceDim)
        else BorderStroke(3.dp, MaterialTheme.colorScheme.surfaceDim)

        OutlinedButton(
            modifier = modifier
                .padding(8.dp)
                .aspectRatio(1f),
            onClick = {
                onClick()
                viewModel.onItemClick(idx)
            },
            shape = RoundedCornerShape(100),
            colors = buttonColors,
            border = border
        ) {}
    }
}