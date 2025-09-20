package com.tomdev.logopadix.presentationLayer.screens.rythm.rythmShelvesScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.R
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.dataLayer.WordContent
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.ImageCard
import com.tomdev.logopadix.presentationLayer.components.RoundsCompletedBox
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.theme.AppTheme


class RythmShelvesScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as com.tomdev.logopadix.LogoApp
        val repo = app.rythmShelvesRepository
        val viewModel: RythmShelvesViewModel by viewModels {
            RythmShelvesViewModelFactory(repo, app)
        }
        setContent {
            AppTheme(ThemeType.THEME_RYTHM.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RythmShelvesScreenRunning(viewModel)
                }
            }
        }
    }


    @Composable
    private fun RythmShelvesScreenRunning(viewModel: RythmShelvesViewModel) {
        AsyncDataWrapper(viewModel = viewModel) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val enabledStates by viewModel.rhymePairsEnabled.collectAsStateWithLifecycle()
            val firstClickedIdx by viewModel.firstIdxClicked.collectAsStateWithLifecycle()
            val secondClickedIdx by viewModel.secondIdxClicked.collectAsStateWithLifecycle()
            val btnEnabled by viewModel.buttonsEnabled.collectAsStateWithLifecycle()

            ScreenWrapper(
                onExit = { finish() },
                title = stringResource(id = R.string.rythm_menu_label_1)
            ) { pdVal ->
                RoundsCompletedBox(
                    viewModel = viewModel,
                    onExit = { finish() }
                ) {
                    Column(
                        modifier = Modifier.padding(start = 18.dp, top = pdVal.calculateTopPadding(), bottom = pdVal.calculateBottomPadding(), end = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.3f)
                                .wrapContentHeight(),
                            text = stringResource(id = R.string.rythm_shelves_label),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f)
                        ) {
                            val rowModifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(top = 9.dp)

                            val imageCardModifier = Modifier
                                .weight(1f)
                                .padding(9.dp)

                            val dividerModifier = Modifier
                                .height(10.dp)
                                .background(colorResource(id = R.color.wood))

                            Row(
                                modifier = rowModifier
                            ) {
                                uiState.firstPart.forEach { obj ->
                                    ShelveCard(
                                        modifier = imageCardModifier,
                                        obj = obj,
                                        objects = uiState.objects,
                                        viewModel = viewModel,
                                        firstClickedIdx = firstClickedIdx,
                                        secondClickedIdx = secondClickedIdx,
                                        enabledStates = enabledStates
                                    )
                                }
                            }
                            HorizontalDivider(modifier = dividerModifier)
                            Row(
                                modifier = rowModifier
                            ) {
                                uiState.secondPart.forEach { obj ->
                                    ShelveCard(
                                        modifier = imageCardModifier,
                                        obj = obj,
                                        objects = uiState.objects,
                                        viewModel = viewModel,
                                        firstClickedIdx = firstClickedIdx,
                                        secondClickedIdx = secondClickedIdx,
                                        enabledStates = enabledStates
                                    )
                                }
                            }
                            HorizontalDivider(modifier = dividerModifier)
                            Row(
                                modifier = rowModifier
                            ) {
                                uiState.thirdPart.forEach { obj ->
                                    ShelveCard(
                                        modifier = imageCardModifier,
                                        obj = obj,
                                        objects = uiState.objects,
                                        viewModel = viewModel,
                                        firstClickedIdx = firstClickedIdx,
                                        secondClickedIdx = secondClickedIdx,
                                        enabledStates = enabledStates
                                    )
                                }
                            }
                            HorizontalDivider(modifier = dividerModifier)
                        }
                        Box(
                            modifier = Modifier.weight(0.4f),
                            contentAlignment = Alignment.BottomCenter
                        ){
                            HorizontalDivider()
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ){
                            Button(
                                enabled = btnEnabled,
                                onClick = { viewModel.onDoneBtnClick() }
                            ) {
                                Text(text = stringResource(id = R.string.done_btn_label))
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun ShelveCard(
        modifier: Modifier,
        obj: WordContent,
        objects: List<WordContent>,
        viewModel: RythmShelvesViewModel,
        firstClickedIdx: Int,
        secondClickedIdx: Int,
        enabledStates: List<Boolean>
    ){
        val imageName = obj.imageName ?: ""
        val drawableId = viewModel.getDrawableId(imageName)
        val soundName = obj.soundName ?: ""
        val soundId = viewModel.getSoundId(soundName)
        val idx = objects.indexOf(obj)
        val isSelected = idx == firstClickedIdx || idx == secondClickedIdx

        ImageCard(
            modifier = modifier.then(
                if (isSelected)
                    Modifier.border(3.dp, Color.Cyan, shape = CardDefaults.shape)
                else Modifier
            ),
            drawable = drawableId,
            enabled = enabledStates[idx],
            onClick = {
                viewModel.onCardClick(idx, soundId)
            }
        )
    }
}