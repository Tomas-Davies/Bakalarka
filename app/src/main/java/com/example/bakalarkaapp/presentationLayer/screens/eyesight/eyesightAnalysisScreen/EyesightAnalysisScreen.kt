package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightAnalysisScreen

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.DragBox
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.EyesightDragDropViewModel
import com.example.bakalarkaapp.presentationLayer.components.dragDrop.EyesightDragDropViewModelFactory
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
                    .fillMaxHeight()
                    .padding(0.dp, it.calculateTopPadding(), 0.dp, 18.dp)
            ) {
                EyesightAnalysisRunning(viewModel)
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun EyesightAnalysisRunning(viewModel: EyesightDragDropViewModel) {
        val ctx = LocalContext.current
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
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.eyesight_drag_drop_label),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Row(
                modifier = modifier
            ) {
                val word = uiState.currentWord
                val wordParts = word.to3Parts()
                // LEFT BOXES
                Column(
                    modifier = modifier,
                ) {
                    val letters = wordParts[0].reversed()
                    for (i in letters.indices){
                        val idx = letters.length-1 - i
                        DragBox(
                            letter = letters[i],
                            enabled = viewModel.enabledStates[idx].value,
                            index = idx
                        )
                    }
                }

                Column(

                ) {
                    Row(

                    ) {

                    }
                    val imageId = resources.getIdentifier(uiState.wordResourcesId, "drawable", ctx.packageName)
                    Image(
                        painter = painterResource(id = R.drawable.dummy_img_500),
                        contentDescription = "image"
                    )
                }

                Column(

                ) {

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