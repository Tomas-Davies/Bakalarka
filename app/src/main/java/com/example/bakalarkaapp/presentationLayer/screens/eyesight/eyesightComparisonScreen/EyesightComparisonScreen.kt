package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.XmlUtils
import com.example.bakalarkaapp.dataLayer.ComparisonData
import com.example.bakalarkaapp.theme.AppTheme

class EyesightComparisonScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme("eyesight") {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val ctx = LocalContext.current
                    val mappedClass = XmlUtils().parseXmlData(
                        ctx,
                        "eyesight_comparison_data",
                        ComparisonData::class.java
                    )
                    val data = mappedClass.data
                    val viewModel: EyesightComparisonViewModel by viewModels {
                        EyesightComparionViewModelFactory(data.shuffled())
                    }
                    val screenState = viewModel.screenState.collectAsState().value
                    when (screenState) {
                        is ScreenState.Running -> EyesightComparisonRunning(viewModel)
                        is ScreenState.Finished -> EyesightComparisonFinished(viewModel)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EyesightComparisonRunning(viewModel: EyesightComparisonViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_menu_label_1)) },
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
            val uiState = viewModel.uiState.collectAsState().value

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val imageResId =
                    resources.getIdentifier(uiState.imageId, "drawable", ctx.packageName)
                Image(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxWidth(),
                    painter = painterResource(id = imageResId),
                    contentDescription = "comparison image"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.eyesight_comparison_label),
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center
                    )
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
                            onClick = {
                                onCompareButtonClick(
                                    true,
                                    uiState.answer,
                                    viewModel,
                                    ctx,
                                    R.raw.correct_answer,
                                    R.raw.wrong_answer
                                )
                            }
                        )
                        Spacer(modifier = Modifier.weight(0.3f))
                        CompareButton(
                            modifier = Modifier.weight(1f),
                            imageId = R.drawable.non_equal,
                            label = stringResource(id = R.string.different),
                            bgColor = colorResource(id = R.color.incorrect),
                            onClick = {
                                onCompareButtonClick(
                                    false,
                                    uiState.answer,
                                    viewModel,
                                    ctx,
                                    R.raw.correct_answer,
                                    R.raw.wrong_answer
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun EyesightComparisonFinished(viewModel: EyesightComparisonViewModel) {
        val ctx = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hotovo",
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { viewModel.restart() }) {
                Text(text = "Znovu")
            }
            Button(onClick = { (ctx as Activity).finish() }) {
                Text(text = "Odejít")
            }
        }
    }

    @Composable
    private fun CompareButton(
        modifier: Modifier,
        imageId: Int,
        label: String,
        bgColor: Color = colorResource(id = R.color.eyesight_300),
        onClick: () -> Unit
    ) {
        ElevatedCard(
            modifier = modifier,
            onClick = { onClick() },
            colors = CardColors(
                contentColor = CardDefaults.cardColors().contentColor,
                containerColor = bgColor,
                disabledContentColor = CardDefaults.cardColors().disabledContentColor,
                disabledContainerColor = CardDefaults.cardColors().disabledContainerColor
            )
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
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorResource(id = R.color.dark)
                )
            }
        }
    }
}

fun onCompareButtonClick(
    userAnswer: Boolean,
    correctAnswer: Boolean,
    viewModel: EyesightComparisonViewModel,
    ctx: Context, correctSoundId: Int,
    wrongSoundId: Int
){
    if (userAnswer == correctAnswer){
        val mp = MediaPlayer.create(ctx, correctSoundId)
        mp.start()
        viewModel.updateData()
    } else {
        val mp = MediaPlayer.create(ctx, wrongSoundId)
        mp.start()
    }
}