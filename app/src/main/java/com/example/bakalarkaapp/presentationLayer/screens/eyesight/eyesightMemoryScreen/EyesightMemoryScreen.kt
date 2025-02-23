package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.bakalarkaapp.presentationLayer.components.RunningOrFinishedRoundScreen
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.components.TimerIndicator
import com.example.bakalarkaapp.theme.AppTheme

class EyesightMemoryScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel: EyesightMemoryViewModel by viewModels {
            EyesightMemoryViewModelFactory(app)
        }
        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightMemoryScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun EyesightMemoryScreenContent(viewModel: EyesightMemoryViewModel) {
        ScreenWrapper(
            headerLabel = stringResource(id = R.string.eyesight_menu_label_4)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
            ) {
                RunningOrFinishedRoundScreen(viewModel = viewModel) {
                    EyesightMemoryRunning(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun EyesightMemoryRunning(viewModel: EyesightMemoryViewModel) {
        val uiState = viewModel.uiState.collectAsState().value

        AnswerResultBox(viewModel = viewModel) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.1f)
                        .wrapContentHeight(),
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
                val enabled = viewModel.buttonsEnabled.collectAsState().value
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ){
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth(),
                        columns = GridCells.Adaptive(150.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        uiState.objectDrawableIds.forEach { name ->
                            val drawable = viewModel.getDrawableId(name)
                            item {
                                ImageCard(
                                    modifier = Modifier.aspectRatio(1f),
                                    drawable = drawable,
                                    onClick = {
                                        if (viewModel.validateAnswer(IValidationAnswer.StringAnswer(name))) {
                                            messageId = R.string.eyesight_memory_label_1
                                        }
                                    },
                                    enabled = enabled
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}