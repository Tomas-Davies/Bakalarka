package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen.imageSearch

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.theme.AppTheme

class EyesightDifferImageSearchScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightImageSearchScreenContent()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EyesightImageSearchScreenContent() {
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
                EyesightAnalysisRunning()
            }
        }
    }

    @Composable
    private fun EyesightAnalysisRunning() {
        ClickableImage()
    }

    @Composable
    fun ClickableImage() {
        var imageSize by remember { mutableStateOf(IntSize.Zero) }
        val screenWidth = LocalConfiguration.current.screenWidthDp

        Image(
            painter = painterResource(id = R.drawable.dummy_img_500),
            contentDescription = "Clickable image",
            modifier = Modifier
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState(screenWidth))
                .onSizeChanged { s -> imageSize = s }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (offset.x >= 0 && offset.x <= imageSize.width
                            &&
                            offset.y >= 0 && offset.y <= imageSize.height
                        ) {
                            val xPercentage = (100 * offset.x) / imageSize.width
                            val yPercentage = (100 * offset.y) / imageSize.height
                        }
                    }
                },

            contentScale = ContentScale.FillHeight
        )

    }


}