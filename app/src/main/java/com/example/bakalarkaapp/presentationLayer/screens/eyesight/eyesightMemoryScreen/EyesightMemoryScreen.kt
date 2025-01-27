package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResult
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.components.TimerIndicator
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.theme.AppTheme

class EyesightMemoryScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as LogoApp
                    val viewModel: EyesightMemoryViewModel by viewModels {
                        EyesightMemoryViewModelFactory(app)
                    }
                    EyesightMemoryScreenContent(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EyesightMemoryScreenContent(viewModel: EyesightMemoryViewModel) {
        val ctx = LocalContext.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_menu_label_4)) },
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
                    is ScreenState.Running -> {
                        EyesightMemoryRunning(viewModel)
                    }

                    is ScreenState.Finished -> {
                        ResultScreen(
                            scorePercentage = viewModel.scorePercentage(),
                            onRestartBtnClick = { viewModel.restart() })
                    }
                }
            }
        }
    }

    @Composable
    private fun EyesightMemoryRunning(viewModel: EyesightMemoryViewModel) {
        val ctx = LocalContext.current
        val uiState = viewModel.uiState.collectAsState().value
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(0.1f),
                    text = "${uiState.round} / ${viewModel.count}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                var messageId by remember { mutableIntStateOf(R.string.eyesight_memory_label_1) }

                TimerIndicator(
                    msDuration = 15000,
                    onFinish = {
                        viewModel.showExtraItem()
                        messageId = R.string.eyesight_memory_label_2
                    },
                    restartTrigger = uiState.round
                )
                Text(
                    modifier = Modifier
                        .weight(0.1f)
                        .wrapContentHeight(),
                    text = stringResource(id = messageId),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth(),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val objects = uiState.objectDrawableIds
                        for (id in objects) {
                            val drawable = resources.getIdentifier(id, "drawable", ctx.packageName)
                            item {
                                ImageCard(
                                    drawable = drawable,
                                    onClick = {
                                        if(viewModel.validateAnswer(id)){
                                            viewModel.playSound(R.raw.correct_answer)
                                            messageId = R.string.eyesight_memory_label_1
                                        } else {
                                            viewModel.playSound(R.raw.wrong_answer)
                                        }
                                    },
                                    enabled = viewModel.enabled
                                )
                            }
                        }
                    }
                }
            }

            AnswerResult(
                viewModel = viewModel,
                modifier = Modifier.align(Alignment.Center)
            )
        }


    }

    @Composable
    private fun ImageCard(
        drawable: Int,
        onClick: () -> Unit,
        enabled: Boolean
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
            onClick = { onClick() },
            enabled = enabled
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = drawable),
                contentDescription = "image"
            )
        }
    }
}