package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen.imageSearch

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.theme.AppTheme

class EyesightSearchScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as LogoApp
                    val viewModel: EyesightSearchViewModel by viewModels {
                        EyesightSearchViewModelFactory(app.eyesightSearchRepository)
                    }
                    EyesightImageSearchScreenContent(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EyesightImageSearchScreenContent(viewModel: EyesightSearchViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_search_label_1)) },
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
                    .padding(0.dp, it.calculateTopPadding(), 0.dp, 0.dp)
            ) {
                val screenState = viewModel.screenState.collectAsState().value
                when (screenState) {
                    is ScreenState.Running -> {
                        EyesightImageSearchRunning(viewModel)
                    }

                    is ScreenState.Finished -> {
                        ResultScreen(
                            scorePercentage = viewModel.scorePercentage(),
                            onRestartBtnClick = { viewModel.restart() })
                    }
                }
            }
        }
    }

    @Composable
    fun EyesightImageSearchRunning(viewModel: EyesightSearchViewModel) {
        val ctx = LocalContext.current
        val uiState = viewModel.uiState.collectAsState().value
        val imageId = resources.getIdentifier(uiState.bgImageResource, "drawable", ctx.packageName)

        var imageSize by remember { mutableStateOf(IntSize.Zero) }
        val initVal = 1f
        var scale by remember { mutableFloatStateOf(initVal) }
        var panningOffset by remember { mutableStateOf(Offset(0f, 0f)) }
        var maxX = 0f
        var maxY = 0f

        BoxWithConstraints {
            val state = rememberTransformableState { zoomChange, panChange, _ ->
                scale *= zoomChange
                scale = scale.coerceIn(initVal, 3f)

                val extraWidth = (scale - initVal) * constraints.maxWidth
                val extraHeight = (scale - initVal) * constraints.maxHeight

                maxX = extraWidth / 2
                maxY = extraHeight / 2

                panningOffset = Offset(
                    x = (panningOffset.x + scale * panChange.x * 0.5f).coerceIn(-maxX, maxX),
                    y = (panningOffset.y + scale * panChange.y * 0.5f).coerceIn(-maxY, maxY)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .transformable(state)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = panningOffset.x
                        translationY = panningOffset.y
                    }
            ) {
                SearchImage(
                    drawableId = imageId,
                    imageSize = imageSize,
                    setImageSize = { newSize -> imageSize = newSize },
                    scale = scale,
                    maxX = maxX,
                    maxY = maxY
                )

                for (item in uiState.items) {
                    key(item) {
                        val r = item.color.r.value.toInt()
                        val g = item.color.g.value.toInt()
                        val b = item.color.b.value.toInt()
                        ItemOverlay(
                            viewModel = viewModel,
                            size = item.size.value.toInt().dp,
                            xPerc = item.x.value.toFloat(),
                            yPerc = item.y.value.toFloat(),
                            imageSize = imageSize,
                            itemColor = Color(r, g, b)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchImage(
        drawableId: Int,
        imageSize: IntSize,
        setImageSize: (newSize: IntSize) -> Unit,
        scale: Float,
        maxX: Float,
        maxY: Float
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Clickable image",
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { setImageSize(it) }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (offset.x >= 0 && offset.x <= imageSize.width
                            &&
                            offset.y >= 0 && offset.y <= imageSize.height
                        ) {
                            val xPercentage =
                                (100 * (offset.x + maxX)) / (imageSize.width * scale)
                            val yPercentage =
                                (100 * (offset.y + maxY)) / (imageSize.height * scale)

                            Log.w("LEVEL_INFO","x = $xPercentage%, y = $yPercentage%")
                        }
                    }
                },
            contentScale = ContentScale.Fit
        )
    }

    @Composable
    private fun ItemOverlay(
        size: Dp,
        viewModel: EyesightSearchViewModel,
        xPerc: Float,
        yPerc: Float,
        imageSize: IntSize,
        itemColor: Color
    ) {
        var trX: Float
        var trY: Float
        val xPos = (xPerc / 100f) * imageSize.width
        val yPos = (yPerc / 100f) * imageSize.height
        var visibility by remember { mutableStateOf(false) }
        var hideItem by remember { mutableStateOf(true) }
        val overlaySize = with(LocalDensity.current){size.toPx()/ 2}
        val overlayLayer = Rect(Offset(overlaySize, overlaySize), overlaySize)
        val modifier = Modifier
            .graphicsLayer {
                trX = xPos - size.toPx() / 2
                trY = yPos - size.toPx() / 2
                translationX = trX
                translationY = trY
            }
            .drawBehind {
                val whiteComplement = Color(-(255 - itemColor.red).toInt(), (255 - itemColor.green).toInt() , (255 - itemColor.blue).toInt())
                val paint = Paint()
                paint.color = whiteComplement
                paint.blendMode = BlendMode.Plus
                drawIntoCanvas {
                    it.drawRect(
                        overlayLayer,
                        paint
                    )
                }
            }
            .drawBehind {
                val paint = Paint()
                paint.color = Color.Black
                paint.blendMode = BlendMode.Saturation
                drawIntoCanvas {
                    it.drawRect(
                        overlayLayer,
                        paint
                    )
                }
            }
            .drawBehind {
                val paint = Paint()
                paint.color = Color.DarkGray
                paint.blendMode = BlendMode.ColorBurn
                drawIntoCanvas {
                    it.drawRect(
                        overlayLayer,
                        paint
                    )
                }
            }
            .size(size)
            .clickable(
                enabled = !visibility
            ) {
                val vibrator = getVibrator()
                vibrator.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                )
                viewModel.onItemClick()
                visibility = true
                hideItem = false
            }

        if (hideItem) {
            Box(
                modifier =
                if (visibility)
                    modifier.border(2.dp, Color.Red, RoundedCornerShape(size))
                else modifier
            ) {}
        }
    }

    private fun getVibrator(): Vibrator {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        return vibrator
    }
}