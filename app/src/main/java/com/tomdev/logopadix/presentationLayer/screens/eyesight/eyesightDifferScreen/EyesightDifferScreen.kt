package com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightDifferScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.R
import com.tomdev.logopadix.presentationLayer.DifficultyType
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.presentationLayer.components.RoundsCompletedBox
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.tomdev.logopadix.services.DayStreakService
import com.tomdev.logopadix.theme.AppTheme

class EyesightDifferScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val levelIndex = intent.getIntExtra(IImageLevel.TAG, 0)
                    val diffId = intent.getStringExtra(DifficultyType.TAG) ?: ""
                    val app = application as com.tomdev.logopadix.LogoApp
                    val repo = app.eyesightDifferRepository
                    val streakService = DayStreakService(app.applicationContext)
                    val viewModel: EyesightDifferViewModel by viewModels {
                        EyesightDifferViewModelFactory(repo, app, levelIndex, diffId, streakService)
                    }
                    EyesightDifferScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun EyesightDifferScreenContent(viewModel: EyesightDifferViewModel){
        var questionSoundName by remember { mutableStateOf("") }
        ScreenWrapper(
            onExit = { finish() },
            showPlaySoundIcon = true,
            soundAssignmentId = if (questionSoundName.isEmpty()) -1 else viewModel.getSoundId(questionSoundName),
            viewModel = viewModel,
            title = stringResource(id = R.string.eyesight_menu_label_3)
        ) {
            AsyncDataWrapper(viewModel) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp, it.calculateTopPadding(), 18.dp, it.calculateBottomPadding()+18.dp)
                ) {
                    EyesightDifferRunning(
                        viewModel = viewModel,
                        setQuestionSoundName = { name -> questionSoundName = name }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun EyesightDifferRunning(
        viewModel: EyesightDifferViewModel,
        setQuestionSoundName: (name: String) -> Unit
    ){
        RoundsCompletedBox(
            viewModel = viewModel,
            onExit = { finish() }
            ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val questionNumber by viewModel.questionNumber.collectAsStateWithLifecycle()
                val questionCount by viewModel.questionCountInRound.collectAsStateWithLifecycle()
                val imageId = viewModel.getDrawableId(uiState.imageName)
                setQuestionSoundName(uiState.questionSoundName)

                Column(
                    modifier = Modifier.weight(3f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f),
                        contentAlignment = Alignment.Center
                    ){
                        if (imageId != 0){
                            AnimatedContent(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                targetState = imageId,
                                label = "",
                                transitionSpec = { slideInHorizontally {fullWidth -> fullWidth } togetherWith  slideOutHorizontally{fullWidth -> -fullWidth } }
                            ) {targetImage ->
                                Image(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.FillWidth,
                                    painter = painterResource(id = targetImage),
                                    contentDescription = "image"
                                )
                            }
                        } else {
                            Image(painter = painterResource(id = R.drawable.image_not_available), contentDescription = "image")
                        }
                    }
                    Text(
                        modifier = Modifier
                            .weight(0.2f)
                            .wrapContentHeight(),
                        text = "$questionNumber / $questionCount",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        text = uiState.question,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                val btnEnabledState by viewModel.buttonsEnabled.collectAsStateWithLifecycle()
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    uiState.answers.forEach { answer ->
                        Button(
                            modifier = Modifier.padding(4.dp, 0.dp),
                            onClick = {
                                viewModel.onBtnClick(answer.objectName)
                            },
                            enabled = btnEnabledState
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = answer.objectName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val drawableId = viewModel.getDrawableId(answer.imageName)
                                Image(
                                    modifier = Modifier.size(38.dp),
                                    painter = painterResource(drawableId),
                                    contentDescription = "decor image"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}