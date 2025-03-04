package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSearch

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.dataLayer.models.ItemColor
import com.example.bakalarkaapp.presentationLayer.components.AnswerResultBox
import com.example.bakalarkaapp.presentationLayer.components.RunningOrFinishedRoundScreen
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
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
            AppTheme(ThemeType.THEME_EYESIGHT.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val levelIdx = intent.getIntExtra(ImageLevel.TAG, 0)
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
            title = stringResource(id = R.string.eyesight_search_label_1)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = it.calculateTopPadding())
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
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var imgWidth by remember { mutableFloatStateOf(0f) }
        var imgHeight by remember { mutableFloatStateOf(0f) }
        var imgSize by remember { mutableStateOf(Size.Zero) }
        var imgOffset by remember { mutableStateOf(Offset.Zero) }
        val imgId = viewModel.getDrawableId(uiState.bgImageResource)

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
                val oInfo = viewModel.getOverlayInfo(item, imgSize, imgOffset)
                key(item) {
                    ItemOverlay(
                        viewModel = viewModel,
                        width = oInfo.width,
                        height = oInfo.height,
                        offset = Offset(
                            oInfo.xInImage,
                            oInfo.yInImage
                        ),
                        itemColor = item.color
                    )
                }
            }
            val showMissIndicator by viewModel.showMissIndicator.collectAsStateWithLifecycle()
            val missIndicatorOffset by viewModel.missIndicatorOffset.collectAsStateWithLifecycle()

            MissIndicator(
                offset = missIndicatorOffset,
                show = showMissIndicator
            )
            ElevatedCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
            ) {
                val found by viewModel.itemsFound.collectAsStateWithLifecycle()
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
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
        itemColor: ItemColor
    ) {
        var overlayShow by remember { mutableStateOf(true) }
        val x = offset.x - width / 2
        val y = offset.y - height / 2
        val xDp = with(LocalDensity.current) { x.toDp() }
        val yDp = with(LocalDensity.current) { y.toDp() }
        val widthDp = with(LocalDensity.current) { width.toDp() }
        val heightDp = with(LocalDensity.current) { height.toDp() }
        val overlayLayer = Rect(Offset(x, y), Size(width, height))

        val filteredColorModifier = Modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val whiteComplement = Color(
                        (255 - itemColor.r),
                        (255 - itemColor.g),
                        (255 - itemColor.b)
                    )
                    val paint = Paint()
                    paint.color = whiteComplement
                    paint.blendMode = BlendMode.Plus
                    canvas.drawRect(
                        overlayLayer,
                        paint
                    )
                    paint.color = Color.Black
                    paint.blendMode = BlendMode.Saturation
                    canvas.drawRect(
                        overlayLayer,
                        paint
                    )
                    paint.color = Color.DarkGray
                    paint.blendMode = BlendMode.ColorBurn
                    canvas.drawRect(
                        overlayLayer,
                        paint
                    )
                }
            }

        val overlayModifier = filteredColorModifier
            .size(DpSize(widthDp, heightDp))
            .offset(xDp, yDp)
            .clickable {
                overlayShow = false
                viewModel.onOverlayClick()
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
                modifier = overlayModifier//.border(2.dp, Color.Red)
            ) {}
        } else {
            Box(
                modifier = Modifier
                    .offset(xDp, yDp)
                    .size(
                        widthDp.times(1.5f),
                        heightDp.times(1.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                KonfettiView(parties = parties, modifier = Modifier.fillMaxSize())
            }
        }
    }
}