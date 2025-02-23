package com.example.bakalarkaapp.presentationLayer.screens.eyesight.imageSearch

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.VibrationEffect
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResultBox
import com.example.bakalarkaapp.presentationLayer.components.RunningOrFinishedRoundScreen
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.utils.image.getContentOffsetInImage
import com.example.bakalarkaapp.utils.image.getFitContentScaleInImage
import com.example.bakalarkaapp.utils.image.getContentSizeInImage
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class EyesightSearchScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val levelIdx = intent.getIntExtra("LEVEL_INDEX", 0)
                    val app = application as LogoApp
                    val viewModel: EyesightSearchViewModel by viewModels {
                        EyesightSearchViewModelFactory(app, levelIdx)
                    }
                    EyesightImageSearchScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun EyesightImageSearchScreenContent(viewModel: EyesightSearchViewModel) {
        ScreenWrapper(
            headerLabel = stringResource(id = R.string.eyesight_search_label_1)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(0.dp, it.calculateTopPadding(), 0.dp, 0.dp)
            ) {
                RunningOrFinishedRoundScreen(
                    viewModel = viewModel,
                    resultPercLabel = stringResource(id = R.string.accuracy_label)
                ) {
                    EyesightImageSearchRunning(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    fun EyesightImageSearchRunning(viewModel: EyesightSearchViewModel) {
        val uiState = viewModel.uiState.collectAsState().value
        val imgId = viewModel.getDrawableId(uiState.bgImageResource)
        var imgWidth by remember { mutableFloatStateOf(0f) }
        var imgHeight by remember { mutableFloatStateOf(0f) }
        var imgSize by remember { mutableStateOf(Size.Zero) }
        var imgOffset by remember { mutableStateOf(Offset.Zero) }

        AnswerResultBox(viewModel = viewModel) {
            SearchImage(
                viewModel = viewModel,
                drawableId = imgId,
                setImageWidth = { width -> imgWidth = width },
                setImageHeight = { height -> imgHeight = height },
                setImageSize = { newSize -> imgSize = newSize },
                setContentOffset = { off -> imgOffset = off }
            )

            uiState.items.forEach { item ->
                val oStats = viewModel.imgPercToSize(item, imgSize, imgOffset)
                key(item) {
                    ItemOverlay(
                        viewModel = viewModel,
                        width = oStats.width,
                        height = oStats.height,
                        offset = Offset(
                            oStats.xInImage,
                            oStats.yInImage
                        ),
                        itemColor = Color
                            (
                            item.color.r,
                            item.color.g,
                            item.color.b
                        )
                    )
                }
            }
            val showMissIndicator = viewModel.showMissIndicator.collectAsState().value
            val missIndicatorOffset = viewModel.missIndicatorOffset.collectAsState().value

            MissIndicator(
                offset = missIndicatorOffset,
                show = showMissIndicator
            )

            ElevatedCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
            ) {
                val found = viewModel.itemsFound.collectAsState().value
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background) // todo change
                        .padding(10.dp),
                    text = "${found}/${uiState.items.size}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    @Composable
    private fun SearchImage(
        viewModel: EyesightSearchViewModel,
        drawableId: Int,
        setImageWidth: (width: Float) -> Unit,
        setImageHeight: (height: Float) -> Unit,
        setImageSize: (newSize: Size) -> Unit,
        setContentOffset: (pos: Offset) -> Unit
    ) {
        val ctx = LocalContext.current
        var contentScale by remember { mutableFloatStateOf(0f) }

        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(ctx.resources, drawableId, options)
        val imgContent = bitmap.asImageBitmap()
        var imgOffset by remember { mutableStateOf(Offset.Zero) }
        Image(
            bitmap = imgContent,
            contentDescription = "Clickable img",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .onGloballyPositioned { cords ->
                    imgOffset = cords.positionInRoot()
                    val imgWidth = cords.size.width.toFloat()
                    val imgHeight = cords.size.height.toFloat()
                    contentScale =
                        getFitContentScaleInImage(imgWidth, imgHeight, imgContent)
                    val contentSize = getContentSizeInImage(imgContent, contentScale)
                    val contentOffset = getContentOffsetInImage(contentSize, imgWidth, imgHeight)
                    setContentOffset(contentOffset)
                    setImageWidth(imgWidth)
                    setImageHeight(imgHeight)
                    setImageSize(contentSize)
                }
                // Pro zjistovani souradnic pri implementaci noveho levelu
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            viewModel.logClickPercInImage(
                                offset,
                                imgOffset,
                                imgContent,
                                contentScale
                            )
                            viewModel.moveMissIndicator(offset)
                            viewModel.vibrate(
                                VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                            )
                            viewModel.playSound(R.raw.wrong_answer)
                            viewModel.missClick()
                        }
                    )
                },
        )
    }

    @Composable
    private fun MissIndicator(offset: Offset, show: Boolean) {
        val boxSize = 30.dp
        val x = with(LocalDensity.current) { offset.x.toDp() - boxSize / 2 }
        val y = with(LocalDensity.current) { offset.y.toDp() - boxSize / 2 }

        Box(
            modifier = Modifier
                .size(boxSize)
                .offset(x, y),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = show,
                enter = scaleIn(
                    animationSpec = tween(durationMillis = 100)
                ),
                exit = scaleOut(
                    animationSpec = tween(durationMillis = 100)
                )
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.miss_click_icon),
                    contentDescription = "miss click icon",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }

    @Composable
    private fun ItemOverlay(
        viewModel: EyesightSearchViewModel,
        height: Float,
        width: Float,
        offset: Offset,
        itemColor: Color
    ) {
        var overlayShow by remember { mutableStateOf(true) }
        var trX: Float
        var trY: Float
        val widthDp = with(LocalDensity.current) { width.toDp() }
        val heightDp = with(LocalDensity.current) { height.toDp() }

        val overlayLayer = Rect(Offset(0f, 0f), Size(width, height))
        val positionModifier = Modifier
            .graphicsLayer {
                trX = offset.x - width / 2
                trY = offset.y - height / 2
                translationX = trX
                translationY = trY
            }

        val filteredColorModifier = positionModifier
            .drawBehind {
                val whiteComplement = Color(
                    -(255 - itemColor.red).toInt(),
                    (255 - itemColor.green).toInt(),
                    (255 - itemColor.blue).toInt()
                )
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

        val modifier = filteredColorModifier
            .size(DpSize(widthDp, heightDp))
            .clickable {
                overlayShow = false
                viewModel.onItemClick()
            }

        val parties = listOf(
            Party(
                speed = 3f,
                damping = 1f,
                spread = 360,
                position = Position.Relative(0.2, 0.2),
                timeToLive = 1000L,
                fadeOutEnabled = true,
                size = listOf(nl.dionsegijn.konfetti.core.models.Size.SMALL),
                emitter = Emitter(duration = 50L, TimeUnit.MILLISECONDS).max(15)
            )
        )

        if (overlayShow) {
            Box(
                modifier = modifier//.border(2.dp, Color.Red)
            ) {}
        } else {
            Box(
                modifier = positionModifier.size(widthDp.times(1.5f), heightDp.times(1.5f)),
                contentAlignment = Alignment.Center
            ) {
                KonfettiView(parties = parties, modifier = Modifier.fillMaxSize())
            }
        }
    }
}