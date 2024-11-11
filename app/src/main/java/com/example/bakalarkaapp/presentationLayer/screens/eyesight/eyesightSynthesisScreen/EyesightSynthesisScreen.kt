@file:OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthesisScreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.DragBox
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.DropBox
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.EyesightDragDropUiState
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.EyesightDragDropViewModel
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.EyesightDragDropViewModelFactory
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.theme.AppTheme

class EyesightSynthesisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val words: Array<String> = resources.getStringArray(R.array.EyesightSynthesisWords)
        words.shuffle()

        val viewModel: EyesightDragDropViewModel by viewModels {
            EyesightDragDropViewModelFactory(words)
        }

        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightSynthesisScreenContent(viewModel)
                }
            }
        }
    }


    @Composable
    private fun EyesightSynthesisScreenContent(viewModel: EyesightDragDropViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_menu_label_5)) },
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
                    is ScreenState.Running -> EyesightSynthesisRunning(ctx, viewModel)
                    is ScreenState.Finished -> ResultScreen(
                        viewModel.scorePercentage(),
                        onRestartBtnClick = { viewModel.restart() }
                    )
                }
            }
        }
    }

    @Composable
    private fun EyesightSynthesisRunning(ctx: Context, viewModel: EyesightDragDropViewModel){
        val uiState = viewModel.uiState.collectAsState().value

        val allLettersPlaced = remember(viewModel.enabledStates) {
            derivedStateOf { viewModel.enabledStates.all { !it.value } }
        }

        LaunchedEffect(allLettersPlaced.value) {
            if (allLettersPlaced.value) {
                viewModel.validateResult()
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.eyesight_drag_drop_label),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.weight(0.1f))

            val imageId = resources.getIdentifier(uiState.wordResourcesId, "drawable", ctx.packageName)

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                painter = painterResource(id = imageId),
                contentDescription = "image"
            )

            DragDropBoxes(
                modifier = Modifier.weight(0.7f),
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }

    @Composable
    private fun DragDropBoxes(
        modifier: Modifier,
        uiState: EyesightDragDropUiState,
        viewModel: EyesightDragDropViewModel
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                maxItemsInEachRow = 5
            ) {
                for (i in uiState.currentWord.indices) {
                    key(i){
                        DropBox(
                            index = i,
                            viewModel = viewModel,
                            toggleDragSourceAbility = { sourceIndex, enabled ->
                                if (sourceIndex != -1 && sourceIndex < viewModel.enabledStates.size) {
                                    viewModel.enabledStates[sourceIndex].value = enabled
                                }
                            }
                        )
                    }
                }
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                maxItemsInEachRow = 5
            ) {
                for (i in uiState.currentWord.indices) {
                    key(i) {
                        DragBox(
                            letter = uiState.currentWord[i],
                            enabled = viewModel.enabledStates[i].value,
                            index = i
                        )
                    }
                }
            }
        }
    }
}