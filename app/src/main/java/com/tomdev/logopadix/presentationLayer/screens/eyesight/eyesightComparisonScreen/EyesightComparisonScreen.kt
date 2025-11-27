package com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightComparisonScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.R
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.presentationLayer.components.LinearTimerIndicator
import com.tomdev.logopadix.presentationLayer.components.RoundsCompletedBox
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.tomdev.logopadix.theme.AppTheme


class EyesightComparisonScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val levelIdx = intent.getIntExtra(IImageLevel.TAG, 0)
        val app = application as com.tomdev.logopadix.LogoApp
        val repo = app.eyesightComparisonRepository
        val viewModel: EyesightComparisonViewModel by viewModels {
            EyesightComparionViewModelFactory(repo, app, levelIdx)
        }
        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightComparisonRunning(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    private fun EyesightComparisonRunning(viewModel: EyesightComparisonViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            showPlaySoundIcon = true,
            soundAssignmentId = R.raw.are_pictures_same_or_not,
            viewModel = viewModel,
            title = stringResource(id = R.string.eyesight_menu_label_1)
        ) {
            AsyncDataWrapper(viewModel) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                RoundsCompletedBox(
                    viewModel = viewModel,
                    onExit = { finish() }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                18.dp,
                                it.calculateTopPadding(),
                                18.dp,
                                it.calculateBottomPadding() + 18.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val enabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()

                        LinearTimerIndicator(
                            msDuration = 15_000,
                            onFinish = {
                                if (enabled) {
                                    viewModel.onTimerFinish()
                                }
                            },
                            restartTrigger = uiState.restartTrigger
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        val imageResId = viewModel.getDrawableId(uiState.imageName)
                        AnimatedContent(
                            modifier = Modifier
                                .weight(1.5f)
                                .fillMaxWidth(),
                            targetState = imageResId,
                            label = "",
                            transitionSpec = { slideInHorizontally { fullWidth -> fullWidth } togetherWith slideOutHorizontally { fullWidth -> -fullWidth } }
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
                            ) {
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
                                    outlineColor = colorResource(id = R.color.correct_outline),
                                    enabled = enabled,
                                    onClick = {
                                        viewModel.onBtnClick(true)
                                    }
                                )
                                Spacer(modifier = Modifier.weight(0.3f))
                                CompareButton(
                                    modifier = Modifier.weight(1f),
                                    imageId = R.drawable.non_equal,
                                    label = stringResource(id = R.string.different),
                                    bgColor = colorResource(id = R.color.incorrect),
                                    outlineColor = colorResource(id = R.color.incorrect_outline),
                                    enabled = enabled,
                                    onClick = {
                                        viewModel.onBtnClick(false)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun CompareButton(
        modifier: Modifier,
        imageId: Int,
        label: String,
        bgColor: Color,
        outlineColor: Color,
        enabled: Boolean,
        onClick: () -> Unit
    ) {
        val cardColors = CardDefaults.cardColors().copy(
            containerColor = bgColor
        )
        CustomCard(
            modifier = modifier,
            onClick = { onClick() },
            colors = cardColors,
            outlineColor = outlineColor,
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