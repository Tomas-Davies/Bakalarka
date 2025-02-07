package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

import android.app.Activity
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResult
import com.example.bakalarkaapp.presentationLayer.components.ImageCard
import com.example.bakalarkaapp.presentationLayer.components.PlaySoundButton
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.theme.AppTheme

class HearingFonematicScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(ThemeType.THEME_HEARING) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HearingFonematicScreenContent(viewModel: HearingFonematicViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.hearing_menu_label_1)) },
                    navigationIcon = {
                        IconButton(onClick = { (ctx as Activity).finish() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back button"
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
            ) {
                val screenState = viewModel.screenState.collectAsState().value
                when (screenState) {
                    is ScreenState.Running -> {
                        HearingFonematicRunning(viewModel)
                    }

                    is ScreenState.Finished -> {
                        ResultScreen(
                            scorePercentage = viewModel.scorePercentage(),
                            onRestartBtnClick = { viewModel.restart() }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun HearingFonematicRunning(viewModel: HearingFonematicViewModel) {
        val uiState = viewModel.uiState.collectAsState().value
        val enabled = viewModel.buttonsEnabled.collectAsState().value
        val soundId = resources.getIdentifier(uiState.playedWord, "raw", packageName)

        Box {
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
                        .fillMaxWidth(0.7f),
                    targetState = uiState.words,
                    label = ""
                ) { targetList ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        targetList.forEach { word ->
                            val drawable = resources.getIdentifier(word, "drawable", packageName)
                            ImageCard(
                                drawable = drawable,
                                enabled = enabled,
                                onClick = { viewModel.validateAnswer(word) }
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
            AnswerResult(
                viewModel = viewModel,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}