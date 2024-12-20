@file:OptIn(ExperimentalLayoutApi::class)

package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightAnalysisScreen

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.DragBox
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.DropBox
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.EyesightDragDropViewModel
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.EyesightDragDropViewModelFactory
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.theme.AppTheme


class EyesightAnalysisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val words: Array<String> = resources.getStringArray(R.array.EyesightAnalysisWords)
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
                    EyesightAnalysisScreenContent(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EyesightAnalysisScreenContent(viewModel: EyesightDragDropViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_menu_label_2)) },
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
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val screenState = viewModel.screenState.collectAsState().value
                when (screenState) {
                    is ScreenState.Running -> EyesightAnalysisRunning(ctx, viewModel)
                    is ScreenState.Finished -> ResultScreen(
                        viewModel.scorePercentage(),
                        onRestartBtnClick = { viewModel.restart() }
                    )
                }
            }
        }
    }

    @Composable
    private fun EyesightAnalysisRunning(ctx: Context, viewModel: EyesightDragDropViewModel) {
        val uiState = viewModel.uiState.collectAsState().value

        val allLettersPlaced = remember(viewModel.enabledStates) {
            derivedStateOf { viewModel.enabledStates.all { !it.value } }
        }

        LaunchedEffect(allLettersPlaced.value) {
            if (allLettersPlaced.value) {
                viewModel.validateResult()
            }
        }
        val modifier = Modifier.fillMaxWidth()

        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.eyesight_drag_drop_label),
            style = MaterialTheme.typography.headlineSmall
        )

        val word = uiState.currendWord
        val wordParts = word.to3Parts()
        val topIndexesStart = wordParts[0].length
        val rightIndexesStart = topIndexesStart + wordParts[1].length

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            // TOP BOXES
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val letters = wordParts[1]
                var position = topIndexesStart
                for (i in letters.indices) {
                    DragBox(
                        letter = letters[i],
                        enabled = viewModel.enabledStates[position].value,
                        index = position
                    )
                    position++
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT BOXES
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    val letters = wordParts[0].reversed()
                    for (i in letters.indices) {
                        val idx = letters.length - 1 - i
                        key(i) {
                            DragBox(
                                letter = letters[i],
                                enabled = viewModel.enabledStates[idx].value,
                                index = idx
                            )
                        }
                    }
                }

                // CENTER IMAGE
                val imageId = ctx.resources.getIdentifier(uiState.wordResourcesId, "drawable", ctx.packageName)
                Image(
                    modifier = modifier
                        .weight(5f),
                    painter = painterResource(id = imageId),
                    contentDescription = "image",
                    contentScale = ContentScale.FillWidth
                )

                // RIGHT BOXES
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    var position = rightIndexesStart
                    val letters = wordParts[2]
                    for (i in letters.indices) {
                        key(i) {
                            DragBox(
                                letter = letters[i],
                                enabled = viewModel.enabledStates[position].value,
                                index = position
                            )
                        }
                        position++
                    }
                }
            }

            FlowRow(
                modifier = modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                maxItemsInEachRow = 6,
                verticalArrangement = Arrangement.Center
            ) {
                for (position in uiState.mixedWord.indices) {
                    key(position) {
                        DropBox(
                            index = position,
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
        }
    }

    private fun String.to3Parts(): List<String> {
        val partSize = this.length / 3
        val remainder = this.length % 3

        val firstEndIdx = partSize + if (remainder > 0) 1 else 0
        val secondEndIdx = firstEndIdx + partSize + if (remainder > 1) 1 else 0

        val parts = listOf(
            this.substring(0, firstEndIdx),
            this.substring(firstEndIdx, secondEndIdx),
            this.substring(secondEndIdx)
        )
        return parts
    }


}