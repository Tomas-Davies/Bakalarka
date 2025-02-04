package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingMemory

import Countdown
import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResult
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.google.accompanist.drawablepainter.rememberDrawablePainter

class HearingMemoryScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel: HearingMemoryViewModel by viewModels {
            HearingMemoryViewModelFactory(app)
        }
        setContent {
            AppTheme(ThemeType.THEME_HEARING) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearingMemoryScreenContent(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HearingMemoryScreenContent(viewModel: HearingMemoryViewModel){
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.hearing_menu_label_2)) },
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
                when(screenState){
                    ScreenState.Running -> HearingMemoryRunning(viewModel)
                    ScreenState.Finished -> ResultScreen(
                        scorePercentage = viewModel.scorePercentage(),
                        onRestartBtnClick = { viewModel.restart() })
                }
            }
        }
    }

    @Composable
    private fun HearingMemoryRunning(viewModel: HearingMemoryViewModel){
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
                    text = "${stringResource(id = R.string.round)}: ${uiState.round}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (uiState.showingImages.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        var showButton by remember(uiState.showingImages.hashCode()) { mutableStateOf(false) }
                        var showSoundPlayingIndicator by remember(uiState.showingImages.hashCode()) { mutableStateOf(false) }
                        if (!showButton){
                            Button(onClick = { showButton = true }) {
                                Text(text = stringResource(id = R.string.play_sound_label))
                            }
                        } else {
                            Countdown(onCountdownFinish = {
                                viewModel.playInitialWords(
                                    onFinish = { showSoundPlayingIndicator = false }
                                )
                                showSoundPlayingIndicator = true
                            })
                        }
                        if (showSoundPlayingIndicator) {
                            Image(
                                modifier = Modifier.fillMaxWidth(0.6f),
                                painter = rememberDrawablePainter(
                                    drawable = getDrawable(R.drawable.sound_playing)
                                ),
                                contentDescription = "animated gif"
                            )
                        }
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp)
                        ) {
                            items(uiState.showingImages){image ->
                                HearingMemoryCard(
                                    drawableName = image,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }

            AnswerResult(viewModel = viewModel)
        }
    }

    @Composable
    private fun HearingMemoryCard(drawableName: String, viewModel: HearingMemoryViewModel){
        val ctx = LocalContext.current
        val drawableId = ctx.resources.getIdentifier(drawableName, "drawable", ctx.packageName)
        var isMarkedAsCorrect by remember(drawableName) { mutableStateOf(false) }
        val cardColors = if (isMarkedAsCorrect) cardColors(
            disabledContainerColor = Color.Green.copy(alpha = 0.5f),
        ) else cardColors()

        Card(
            modifier = Modifier.padding(9.dp),
            onClick = {
                isMarkedAsCorrect = viewModel.validateAnswer(drawableName)
            },
            colors = cardColors,
            enabled = !isMarkedAsCorrect
        ) {
            Image(
                painter = painterResource(id = drawableId),
                contentScale = ContentScale.Inside,
                contentDescription = "hearing memory image"
            )
        }
    }

}