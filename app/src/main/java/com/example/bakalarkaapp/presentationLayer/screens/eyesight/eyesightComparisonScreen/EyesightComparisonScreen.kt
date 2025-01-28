package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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

class EyesightComparisonScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val levelIdx = intent.getIntExtra("LEVEL_INDEX", 0)
                    val app = application as LogoApp
                    val viewModel: EyesightComparisonViewModel by viewModels {
                        EyesightComparionViewModelFactory(app, levelIdx)
                    }
                    val screenState = viewModel.screenState.collectAsState().value
                    when (screenState) {
                        is ScreenState.Running -> EyesightComparisonRunning(viewModel)
                        is ScreenState.Finished -> ResultScreen(
                            viewModel.scorePercentage(),
                            onRestartBtnClick = { viewModel.restart() }
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    @Composable
    private fun EyesightComparisonRunning(viewModel: EyesightComparisonViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_menu_label_1)) },
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
            val uiState = viewModel.uiState.collectAsState().value
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val enabled = viewModel.buttonsEnabled.collectAsState().value

                    TimerIndicator(
                        msDuration = 15000,
                        onFinish = {
                            if(enabled){
                                viewModel.playSound(R.raw.wrong_answer)
                                viewModel.onTimerFinish()
                            }
                        },
                        restartTrigger = uiState.restartTrigger
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    val imageResId = resources.getIdentifier(uiState.imageId, "drawable", ctx.packageName)
                    AnimatedContent(
                        modifier = Modifier
                            .weight(1.5f)
                            .fillMaxWidth(),
                        targetState = imageResId,
                        label = "",
                        transitionSpec = { slideInHorizontally {fullWidth -> fullWidth } togetherWith  slideOutHorizontally{fullWidth -> -fullWidth } }
                    ) { targetImage ->
                        Image(
                            modifier = Modifier
                                .background(Color.White),
                            painter = painterResource(id = targetImage),
                            contentDescription = "comparison image"
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(id = R.string.eyesight_comparison_label),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            CompareButton(
                                modifier = Modifier.weight(1f),
                                imageId = R.drawable.equals,
                                label = stringResource(id = R.string.identical),
                                bgColor = colorResource(id = R.color.correct),
                                enabled = enabled,
                                onClick = {
                                    onCompareButtonClick(
                                        true,
                                        viewModel,
                                        R.raw.correct_answer,
                                        R.raw.wrong_answer
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.weight(0.3f))
                            CompareButton(
                                modifier = Modifier.weight(1f),
                                imageId = R.drawable.non_equal,
                                label = stringResource(id = R.string.different),
                                bgColor = colorResource(id = R.color.incorrect),
                                enabled = enabled,
                                onClick = {
                                    onCompareButtonClick(
                                        false,
                                        viewModel,
                                        R.raw.correct_answer,
                                        R.raw.wrong_answer
                                    )
                                }
                            )
                        }
                    }
                }
                AnswerResult(
                    viewModel = viewModel,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }


    @Composable
    private fun CompareButton(
        modifier: Modifier,
        imageId: Int,
        label: String,
        bgColor: Color = colorResource(id = R.color.eyesight_300),
        enabled: Boolean,
        onClick: () -> Unit
    ) {
        ElevatedCard(
            modifier = modifier,
            onClick = { onClick() },
            colors = CardColors(
                contentColor = CardDefaults.cardColors().contentColor,
                containerColor = bgColor,
                disabledContentColor = CardDefaults.cardColors().disabledContentColor,
                disabledContainerColor = CardDefaults.cardColors().disabledContainerColor
            ),
            enabled = enabled
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    modifier = Modifier.scale(3f),
                    painter = painterResource(id = imageId),
                    contentDescription = "compare button"
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorResource(id = R.color.dark)
                )
            }
        }
    }
}

fun onCompareButtonClick(
    userAnswer: Boolean,
    viewModel: EyesightComparisonViewModel,
    correctSoundId: Int,
    wrongSoundId: Int
) {
    if (viewModel.validateAnswer(userAnswer)){
        viewModel.playSound(correctSoundId)
    } else {
        viewModel.playSound(wrongSoundId)
    }
}