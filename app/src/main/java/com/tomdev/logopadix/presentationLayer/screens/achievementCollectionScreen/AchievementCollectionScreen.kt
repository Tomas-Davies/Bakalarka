package com.tomdev.logopadix.presentationLayer.screens.achievementCollectionScreen

import androidx.compose.foundation.lazy.grid.items
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.repositories.Achievement
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.theme.AppTheme

class AchievementCollectionScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repo = app.achievementsRepo
        val viewModel: AchievementCollectionViewModel by viewModels {
            AchievementCollectionViewModelFactory(app, repo)
        }
        val dayStreak = intent.getIntExtra("DAY_STREAK", 0)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWrapper(
                        title = stringResource(R.string.achievement_badges_heading),
                        onExit = { finish() }
                    ) { pdVal ->
                        AsyncDataWrapper(viewModel) {
                            AchievementCollectionContent(viewModel, pdVal, dayStreak)
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun AchievementCollectionContent(viewModel: AchievementCollectionViewModel, pdVal: PaddingValues, dayStreak: Int){
        val uiState by viewModel.achievementsUiState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .padding(top = pdVal.calculateTopPadding(), start = 18.dp, end = 18.dp, bottom = pdVal.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.achievement_badges_subheading),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(18.dp))
            AchievementCards(
                viewModel = viewModel,
                uiState = uiState,
                dayStreak = dayStreak
            )
        }
    }


    @Composable
    fun AchievementCards(
        viewModel: AchievementCollectionViewModel,
        uiState: AchievementUiState,
        dayStreak: Int
    ){
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(minSize = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(uiState.achievements){ achievement ->
                AchievementCard(
                    viewModel = viewModel,
                    achievement = achievement,
                    dayStreak = dayStreak
                )
            }
        }
    }


    @Composable
    fun AchievementCard(
        viewModel: AchievementCollectionViewModel,
        achievement: Achievement,
        dayStreak: Int
    ){
        val dayStreakFloat = (dayStreak / achievement.costPoints.toFloat()).coerceIn(0f, 1f)
        val animatedProgress by
        animateFloatAsState(
            targetValue = dayStreakFloat,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
        CustomCard(
            onClick = {},
            enabled = dayStreakFloat >= 1f
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(all = 9.dp)
                    .height(8.dp),
                progress = { animatedProgress },
                color = Color.Yellow,
                trackColor = Color.Gray,
                strokeCap = StrokeCap.Round,
                gapSize = 4.dp
            )
            val drawableId = viewModel.getDrawableId(achievement.imageName)
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                painter = painterResource(drawableId),
                contentDescription = "decor image"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${achievement.costPoints}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.hearing_200)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = achievement.label,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}