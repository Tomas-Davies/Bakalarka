@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightAnalysisScreen

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.theme.AppTheme

class EyesightAnalysisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val words: Array<String> = resources.getStringArray(R.array.EyesightAnalysisWords)
        words.shuffle()

        val viewModel: EyesightAnalysisViewModel by viewModels {
            EyesightAnalysisViewModelFacory(words)
        }

        setContent {
            AppTheme("eyesight") {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightAnalysisScreenContent(viewModel)
                }
            }
        }
    }


    @Composable
    private fun EyesightAnalysisScreenContent(viewModel: EyesightAnalysisViewModel) {
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
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
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
    private fun EyesightAnalysisRunning(ctx: Context, viewModel: EyesightAnalysisViewModel){
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
                text = stringResource(id = R.string.eyesightAnalysisLabel),
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
        uiState: EyesightAnalysisUiState,
        viewModel: EyesightAnalysisViewModel
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                maxItemsInEachRow = 5
            ) {
                for (i in uiState.currentWord.indices) {
                    key(i){
                        DropBox(
                            index = i,
                            viewModel = viewModel,
                            toggleDragSourceEnability = { sourceIndex, enabled ->
                                if (sourceIndex != -1 && sourceIndex < viewModel.enabledStates.size) {
                                    viewModel.enabledStates[sourceIndex].value = enabled
                                }
                            }
                        )
                    }
                }
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
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

    @Composable
    private fun DropBox(
        index: Int,
        viewModel: EyesightAnalysisViewModel,
        toggleDragSourceEnability: (index: Int, enabled: Boolean) -> Unit
    ) {
        val resetDropFlag = viewModel.resetDropFlag.collectAsState()
        var dragSourceIndex by remember { mutableIntStateOf(-1) }
        var label by remember { mutableStateOf(" ") }

        LaunchedEffect(resetDropFlag.value) {
            if (label != " "){
                label = " "
                dragSourceIndex = -1
                viewModel.setResetDropFlag(false)
            }
        }

        val callback = remember(resetDropFlag.value) {
            object : DragAndDropTarget {
                override fun onDrop(event: DragAndDropEvent): Boolean {
                    val text = event.toAndroidDragEvent().clipData
                        ?.getItemAt(0)?.text
                    val dataList = text.toString().split('|')
                    dragSourceIndex = dataList[0].toInt()
                    toggleDragSourceEnability(dragSourceIndex, false)
                    label = dataList[1]
                    viewModel.addLetterAt(label[0], index)
                    return true
                }
            }
        }

        Card(
            modifier = Modifier
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { event ->
                        event
                            .mimeTypes()
                            .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    },
                    target = callback
                ),
            onClick = {
                if (label != " "){
                    label = " "
                    toggleDragSourceEnability(dragSourceIndex, true)
                    viewModel.removeLetterAt(index)
                    dragSourceIndex = -1
                }
            }
        ) {
            Text(
                modifier = Modifier.padding(15.dp),
                text = label,
                fontSize = 42.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }


    @Composable
    private fun DragBox(
        letter: Char,
        enabled: Boolean,
        index: Int
    ) {
        Card(
            modifier = Modifier
                .then(
                    if (enabled) {
                        Modifier.dragAndDropSource(
//                    drawDragDecoration = {
//
//                    }
                        ) {
                            detectTapGestures(
                                onPress = {
                                    tryAwaitRelease()
                                    startTransfer(
                                        transferData = DragAndDropTransferData(
                                            ClipData.newPlainText(
                                                "text",
                                                "$index|$letter"
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    } else Modifier.alpha(0.5f)
                )
                .border(
                    BorderStroke(2.dp, if (enabled) Color.Green else Color.Gray),
                    CardDefaults.shape
                )
        ) {
            Text(
                modifier = Modifier
                    .padding(15.dp),
                text = letter.toString(),
                fontSize = 28.sp
            )
        }
    }
}