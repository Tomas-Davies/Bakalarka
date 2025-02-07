package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingSynthesis

import android.app.Activity
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

class HearingSynthesisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel: HearingSynthesisViewModel by viewModels {
            HearingSynthesisViewModelFactory(app)
        }
        setContent {
            AppTheme(ThemeType.THEME_HEARING) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearingSynthScreenContent(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HearingSynthScreenContent(viewModel: HearingSynthesisViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.hearing_menu_label_3)) },
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
                    .fillMaxSize()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
            ) {
                val screenState = viewModel.screenState.collectAsState().value
                when (screenState) {
                    ScreenState.Running -> HearingSynthRunning(viewModel)
                    ScreenState.Finished -> ResultScreen(
                        scorePercentage = viewModel.scorePercentage(),
                        onRestartBtnClick = { viewModel.restart() })
                }
            }
        }
    }

    @Composable
    private fun HearingSynthRunning(viewModel: HearingSynthesisViewModel) {
        val uiState = viewModel.uiState.collectAsState().value
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
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
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f),
                    columns = GridCells.Adaptive(150.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(uiState.words) { word ->
                        val enabled = viewModel.buttonsEnabled.collectAsState().value
                        val drawable = resources.getIdentifier(word, "drawable", packageName)
                        ImageCard(
                            drawable = drawable,
                            enabled = enabled,
                            onClick = { viewModel.validateAnswer(word) },
                            modifier = Modifier.aspectRatio(1f),
                            imageModifier = Modifier.fillMaxSize()
                        )
                    }
                }
                // TODO <nazev>_spelling
                val soundId = resources.getIdentifier(uiState.initWord, "raw", packageName)
                PlaySoundButton(
                    onClick = {
                        viewModel.playSound(soundId)
                        viewModel.enableButtons()
                    }
                )
            }

            AnswerResult(viewModel = viewModel)
        }
    }
}