package com.tomdev.logopadix.presentationLayer.screens.achievements

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.R
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.presentationLayer.screens.achievementCollectionScreen.AchievementCollectionScreen
import com.tomdev.logopadix.presentationLayer.screens.achievementSticker.AchievementStickerScreen
import com.tomdev.logopadix.theme.AppTheme

class AchievementScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel: AchievementViewModel by viewModels {
            AchievementViewModelFactory(app)
        }
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWrapper(
                        title = stringResource(R.string.achievement_heading),
                        onExit = { finish() }
                    ) { pdVal ->
                        AchievementContent(viewModel, pdVal)
                    }
                }
            }
        }
    }


    @Composable
    fun AchievementContent(viewModel: AchievementViewModel, pdVal: PaddingValues){
        val dayStreak by viewModel.dayStreakFlow.collectAsStateWithLifecycle(0)

        Column(
            modifier = Modifier
                .padding(pdVal)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f),
                contentAlignment = Alignment.Center
            ) {
                val initialValue = ((dayStreak - 1) % 7 + 1) / 7f
                StreakIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.5f),
                    targetValue = initialValue
                )
                Text(
                    text = "$dayStreak",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
            // TODO dny v tydnu radek

            // TODO udelat z toho button
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    ImageCard(
                        text = stringResource(R.string.achievement_badges_heading),
                        onClick = { openActivity(0) },
                        painter = painterResource(R.drawable.achievement_icon_1)
                    )
                }
                item {
                    ImageCard(
                        text = stringResource(R.string.achievement_sticker_heading),
                        onClick = { openActivity(1) },
                        painter = painterResource(R.drawable.achievement_stickers_logo)
                    )
                }
            }
                MedalStats()
        }
    }


    @Composable
    fun MedalStats(){

    }


    @Composable
    fun StreakIndicator(
        modifier: Modifier = Modifier,
        targetValue: Float
    ){
        var progress by remember { mutableFloatStateOf(0f) }
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            label = "streak_indicator_progress"
        )
        Box(
            modifier = modifier
                .aspectRatio(1f)

        ){
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = { animatedProgress },
                color = Color.Yellow,
                strokeWidth = 9.dp,
                trackColor = Color.Gray,
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
            )
            Image(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 19.dp)
                    .scale(1.2f),
                painter = painterResource(R.drawable.present_icon),
                contentDescription = ""
            )
        }


        LaunchedEffect(targetValue) {
            progress = targetValue
        }
    }


    @Composable
    fun ImageCard(
        text: String,
        onClick: () -> Unit,
        painter: Painter
    ){
        CustomCard(
            modifier = Modifier
                .sizeIn(maxHeight = 100.dp),
            onClick = { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 9.dp, horizontal = 24.dp),
                    text = text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(1f))
                Image(
                    modifier = Modifier,
                    painter = painter,
                    contentDescription = "word image",
                    contentScale = ContentScale.Fit
                )
            }
        }
    }


    private fun openActivity(idx: Int){
        var intent = Intent(this, AchievementCollectionScreen::class.java)
        when {
            idx == 1 -> { intent = Intent(this, AchievementStickerScreen::class.java) }
        }
        startActivity(intent)
    }
}