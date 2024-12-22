package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.key
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
import com.example.bakalarkaapp.presentationLayer.components.PlaySoundButton
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.theme.AppTheme

class HearingFonematicScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(ThemeType.THEME_HEARING) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as LogoApp
                    val viewModel: HearingFonematicViewModel by viewModels {
                        HearingFonematicFactory(app)
                    }
                    HearingFonematicScreenContent(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HearingFonematicScreenContent(viewModel: HearingFonematicViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.hearing_menu_label_1)) },
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
                    .fillMaxHeight()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
            ) {
                val screenState = viewModel.screenState.collectAsState().value
                when (screenState) {
                    is ScreenState.Running -> {
                        HearingFonematicRunning(viewModel)
                    }

                    is ScreenState.Finished -> {
                        ResultScreen(
                            scorePercentage = viewModel.scorePercentage(),
                            onRestartBtnClick = { viewModel.restart() }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun HearingFonematicRunning(viewModel: HearingFonematicViewModel) {
        val ctx = LocalContext.current
        val uiState = viewModel.uiState.collectAsState().value
        val imageId1 = resources.getIdentifier(uiState.imageResource1, "drawable", ctx.packageName)
        val imageId2 = resources.getIdentifier(uiState.imageResource2, "drawable", ctx.packageName)
        val imageId3 = resources.getIdentifier(uiState.imageResource3, "drawable", ctx.packageName)
        val soundId = resources.getIdentifier(uiState.soundResource, "raw", ctx.packageName)
        val images = mapOf(
            Pair(uiState.imageResource1, imageId1),
            Pair(uiState.imageResource2, imageId2),
            Pair(uiState.imageResource3, imageId3)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(id = R.string.hearing_fonematic_label),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                images.forEach { image ->
                    key(image.value) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .clickable {
                                    val result = viewModel.validateAnswer(image.key)
                                    viewModel.playResultSound(result)
                                },
                            painter = painterResource(id = image.value),
                            contentDescription = "image"
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }

            PlaySoundButton(
                onClick = { viewModel.playSound(soundId) }
            )
        }
    }
}