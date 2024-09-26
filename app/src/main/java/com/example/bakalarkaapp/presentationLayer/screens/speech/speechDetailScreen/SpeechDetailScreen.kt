package com.example.bakalarkaapp.presentationLayer.screens.speech.speechDetailScreen

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ui.theme.AppTheme

class SpeechDetailScreen: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val label = intent.getStringExtra("LABEL") ?: ""
        var words = emptyArray<String>()
        val id = resources.getIdentifier(label, "array", this.packageName)
        if (id != 0) words = resources.getStringArray(id)

        val viewModel: SpeechDetailViewModel by viewModels {
            SpeechDetailViewModelFactory(words)
        }

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SpeechDetailContent(viewModel)
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SpeechDetailContent(viewModel: SpeechDetailViewModel){
        val ctx = LocalContext.current
        val activity = (ctx as Activity)
        val label = activity.intent.getStringExtra("LABEL") ?: ""

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.category_speech) + " - " + label) },
                    navigationIcon = {
                        IconButton(onClick = { activity.finish() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back button"
                            )
                        }
                    }
                )
            }
        ){pdVal ->
            SpeechDetail(pdVal, viewModel)
        }
    }

    @Composable
    private fun SpeechDetail(
        pdVal: PaddingValues,
        viewModel: SpeechDetailViewModel
    ){
        val ctx = LocalContext.current
        val uiState = viewModel.uiState.collectAsState().value
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp, pdVal.calculateTopPadding(), 15.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val imageId = resources.getIdentifier(uiState.id, "drawable", ctx.packageName)

            Column(
                modifier = Modifier.weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(3f),
                    contentAlignment = Alignment.Center
                ){
                    Image(painter = painterResource(id = imageId), contentDescription = "image")
                }
                Text(
                    modifier = Modifier.weight(1f).wrapContentHeight(),
                    text = uiState.currentWord,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.previous() },
                    enabled = !uiState.isOnFirstWord
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "button previous")
                }

                val audioId = resources.getIdentifier(uiState.id, "raw", ctx.packageName)
                val mp = MediaPlayer.create(ctx, audioId)

                Button(onClick = { mp.start() }) {
                    Text(text = stringResource(id = R.string.play_sound_label))
                }
                IconButton(
                    onClick = { viewModel.next() },
                    enabled = !uiState.isOnLastWord
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "button next")
                }
            }
        }
    }
}