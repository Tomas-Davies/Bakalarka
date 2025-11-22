package com.tomdev.logopadix.presentationLayer.screens.hearing.hearingMemory

import Countdown
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.tomdev.logopadix.theme.AppTheme
import com.tomdev.logopadix.R
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.presentationLayer.components.RoundsCompletedBox
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch


class HearingMemoryScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as com.tomdev.logopadix.LogoApp
        val repo = app.hearingMemoryRepository
        val viewModel: HearingMemoryViewModel by viewModels {
            HearingMemoryViewModelFactory(repo, app)
        }
        setContent {
            AppTheme(ThemeType.THEME_HEARING.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearingMemoryScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun HearingMemoryScreenContent(viewModel: HearingMemoryViewModel){
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.hearing_menu_label_2)
        ) {
            AsyncDataWrapper(viewModel = viewModel) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp, it.calculateTopPadding(), 18.dp, it.calculateBottomPadding()+18.dp)
                ) {
                    HearingMemoryRunning(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun HearingMemoryRunning(viewModel: HearingMemoryViewModel){
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        RoundsCompletedBox(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            contentAlignment = Alignment.Center,
            onExit = { finish() }
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

                if (uiState.showingObjects.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = stringResource(id = R.string.hearing_memory_label_1),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        
                        var showButton by remember(uiState.showingObjects) { mutableStateOf(false) }
                        var showSoundPlayingIndicator by remember(uiState.showingObjects) { mutableStateOf(false) }
                        if (!showButton){
                            Button(onClick = { showButton = true }) {
                                Text(text = stringResource(id = R.string.play_sound_label))
                            }
                        } else {
                            Countdown(onCountdownFinish = {
                                viewModel.playInitallObjects (
                                    onFinish = { showSoundPlayingIndicator = false }
                                )
                                showSoundPlayingIndicator = true
                            })
                        }
                        if (showSoundPlayingIndicator) {
                            Image(
                                modifier = Modifier.fillMaxWidth(0.6f),
                                painter = rememberDrawablePainter(
                                    drawable = AppCompatResources.getDrawable(LocalContext.current, R.drawable.sound_playing)
                                ),
                                contentDescription = "animated gif"
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = stringResource(id = R.string.hearing_memory_label_2),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 150.dp),
                                verticalArrangement = Arrangement.spacedBy(18.dp),
                                horizontalArrangement = Arrangement.spacedBy(18.dp)
                            ) {
                                items(uiState.showingObjects){ obj ->
                                    val drawableName = obj.imageName ?: ""
                                    HearingMemoryCard(
                                        drawableName = drawableName,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HearingMemoryCard(drawableName: String, viewModel: HearingMemoryViewModel){
        val drawableId = viewModel.getDrawableId(drawableName)
        var isMarkedAsCorrect by remember(drawableName) { mutableStateOf(false) }
        val cardColors = if (isMarkedAsCorrect) cardColors().copy(
            disabledContainerColor = colorResource(id = R.color.correct),
        ) else cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        val enabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()

        CustomCard(
            modifier = Modifier.aspectRatio(1f),
            onClick = {
                viewModel.viewModelScope.launch {
                    isMarkedAsCorrect = viewModel.onCardClick(drawableName)
                }
            },
            colors = cardColors,
            enabled = !isMarkedAsCorrect && enabled
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = drawableId),
                contentScale = ContentScale.Inside,
                contentDescription = "hearing memory image"
            )
        }
    }

}