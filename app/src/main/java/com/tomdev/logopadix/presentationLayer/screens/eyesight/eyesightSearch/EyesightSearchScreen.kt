package com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightSearch

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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.fastRoundToInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.R
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.RoundsCompletedBox
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.tomdev.logopadix.theme.AppTheme
import com.tomdev.logopadix.utils.image.getContentOffsetInImage
import com.tomdev.logopadix.utils.image.getFitContentScaleInImage
import com.tomdev.logopadix.utils.image.getContentSizeInImage
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
                    val levelIdx = intent.getIntExtra(IImageLevel.TAG, 0)
                    val app = application as com.tomdev.logopadix.LogoApp
                    val viewModel: EyesightSearchViewModel by viewModels {
                        EyesightSearchViewModelFactory(app.eyesightSearchRepository, app, levelIdx)
                    }
                    EyesightImageSearchScreenContent(viewModel)
                }
            }
        }
    }

    @Composable
    private fun EyesightImageSearchScreenContent(viewModel: EyesightSearchViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            showPlaySoundIcon = true,
            soundAssignmentId = R.raw.find_hidden_cats,
            viewModel = viewModel,
            title = stringResource(id = R.string.eyesight_search_label_1)
        ) {
            AsyncDataWrapper(viewModel = viewModel) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                ) {
                    EyesightImageSearchRunning(viewModel = viewModel)
                }
            }
        }
    }


    @Composable
    fun EyesightImageSearchRunning(viewModel: EyesightSearchViewModel) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var imageContentSize by remember { mutableStateOf(Size.Zero) }
        var imageContentOffset by remember { mutableStateOf(Offset.Zero) }
        var imageContentScale by remember { mutableFloatStateOf(0f) }
        var imageComposableSize by remember { mutableStateOf(Size.Zero) }
        val imageId = viewModel.getDrawableId(uiState.bgImageResource)

        RoundsCompletedBox(
            viewModel = viewModel,
            onExit = { this@EyesightSearchScreen.finish() }
        ) {
            val imageModifier = Modifier
                .fillMaxSize()
                .background(Color.White)

            SearchImage(
                modifier = imageModifier,
                viewModel = viewModel,
                drawableId = imageId,
                setContentSize = { size -> imageContentSize = size },
                setContentOffset = { off -> imageContentOffset = off },
                setContentScale = { scale -> imageContentScale = scale },
                setImageContainerSize = { size -> imageComposableSize = size }
            )
            val nonColoredImageId = viewModel.getDrawableId(uiState.nonColorBgImageResource)
            val options = BitmapFactory.Options()
            options.inScaled = false
            val bitmap = BitmapFactory.decodeResource(resources, nonColoredImageId, options)
            val nonColoredImage = bitmap.asImageBitmap()

            if (imageComposableSize != Size.Zero && imageContentScale != 0f) {
                uiState.items.forEach { item ->
                    val oInfo = viewModel.getOverlayInfo(item, imageContentSize, imageContentOffset)
                    val imageComposableInnerSpace = Size(
                        width = imageComposableSize.width - imageContentSize.width,
                        height = imageComposableSize.height - imageContentSize.height
                    )
                    key(item) {
                        ItemOverlay(
                            viewModel = viewModel,
                            width = oInfo.width,
                            height = oInfo.height,
                            offset = Offset(
                                oInfo.xInImage,
                                oInfo.yInImage
                            ),
                            nonColoredImage = nonColoredImage,
                            contentScale = imageContentScale,
                            imageComposableInnerSpace = imageComposableInnerSpace
                        )
                    }
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


    /**
     * A composable that displays interactive search image and handles its layout calculations.
     *
     * This component renders a bitmap image from a drawable resource and calculates various
     * positioning metrics needed for the overlay components in the eyesight search.
     *
     * @param viewModel Provides responses for images tap gestures.
     * @param drawableId The Id of drawable resource file.
     * @param setContentSize Callback that receives the calculated [Size] of the content inside the
     * image.
     * @param setContentOffset Callback that receives the calculated [Offset] of the content inside
     * the image.
     * @param setContentScale Callback that receives the calculated scale of the content inside
     * the image.
     */
    @Composable
    private fun SearchImage(
        modifier: Modifier = Modifier,
        viewModel: EyesightSearchViewModel,
        drawableId: Int,
        setContentSize: (size: Size) -> Unit,
        setContentOffset: (pos: Offset) -> Unit,
        setContentScale: (scale: Float) -> Unit,
        setImageContainerSize: (size: Size) -> Unit
    ) {
        val ctx = LocalContext.current
        var contentScale by remember { mutableFloatStateOf(0f) }
        var imageOffset by remember { mutableStateOf(Offset.Zero) }
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(ctx.resources, drawableId, options)
        val imageContent = bitmap.asImageBitmap()

        Image(
            bitmap = imageContent,
            contentDescription = "Clickable image",
            modifier = modifier
                .onGloballyPositioned { cords ->
                    imageOffset = cords.positionInRoot()
                    val imageWidth = cords.size.width.toFloat()
                    val imageHeight = cords.size.height.toFloat()
                    contentScale = getFitContentScaleInImage(imageWidth, imageHeight, imageContent)
                    val contentSize = getContentSizeInImage(imageContent, contentScale)
                    val contentOffset = getContentOffsetInImage(contentSize, imageWidth, imageHeight)
                    setContentScale(contentScale)
                    setContentOffset(contentOffset)
                    setContentSize(contentSize)
                    setImageContainerSize(cords.size.toSize())
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            viewModel.logClickPercInImage(
                                offset,
                                imageOffset,
                                imageContent,
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


    /**
     * A composable that displays a red cross for miss click indication.
     *
     * @param offset The [Offset] of the indicator.
     * @param show Boolean flag that controls the visibility of the indicator.
     */
    @Composable
    private fun MissIndicator(
        offset: Offset,
        show: Boolean
    ) {
        val boxSize = 30.dp
        val x = with(LocalDensity.current) { offset.x.toDp() - boxSize / 2 }
        val y = with(LocalDensity.current) { offset.y.toDp() - boxSize / 2 }

        AnimatedVisibility(
            modifier = Modifier
                .size(boxSize)
                .offset(x, y),
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

    /**
     * A composable that displays an overlay to hide the search item.
     *
     * The overlay hides colors under it by drawing parts from non-colored image into the colored image.
     *
     * @param viewModel An [EyesightSearchViewModel] that processes overlay click events.
     * @param width Width of the overlay in pixels.
     * @param height Height of the overlay in pixels.
     * @param offset Offset of the overlay, relative to the image content,
     * @param nonColoredImage The non-colored version of the *main image*.
     * @param contentScale The scale factor that was applied to the content inside Image composable container with ContentScale.Fit applied.
     */
    @Composable
    private fun ItemOverlay(
        viewModel: EyesightSearchViewModel,
        width: Float,
        height: Float,
        offset: Offset,
        nonColoredImage: ImageBitmap,
        contentScale: Float,
        imageComposableInnerSpace: Size
    ) {
        var overlayShow by remember { mutableStateOf(true) }
        val x = offset.x - width / 2
        val y = offset.y - height / 2
        val localDensity = LocalDensity.current
        val xDp = with(localDensity) { x.toDp() }
        val yDp = with(localDensity) { y.toDp() }
        val widthDp = with(localDensity) { width.toDp() }
        val heightDp = with(localDensity) { height.toDp() }

        val overlayModifier = Modifier
            .size(DpSize(widthDp, heightDp))
            .offset(xDp, yDp)
            .clickable {
                overlayShow = false
                viewModel.onOverlayClick()
            }
       //     .border(2.dp, Color.Red)


        if (overlayShow) {
            Canvas(modifier = overlayModifier) {
                val yOffsetAdjustment = (imageComposableInnerSpace.height / 2).fastRoundToInt()
                val xOffsetAdjustment = (imageComposableInnerSpace.width / 2).fastRoundToInt()
                val srcX = ((x - xOffsetAdjustment) / contentScale).fastRoundToInt()
                val srcY = ((y - yOffsetAdjustment) / contentScale).fastRoundToInt()
                val srcSize = IntSize(
                    width = (width / contentScale).fastRoundToInt(),
                    height = (height / contentScale).fastRoundToInt()
                )
                drawImage(
                    image = nonColoredImage,
                    srcOffset = IntOffset(srcX, srcY),
                    srcSize = srcSize,
                    dstOffset = IntOffset.Zero,
                    dstSize = IntSize(width.fastRoundToInt(), height.fastRoundToInt())
                )
            }
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
               KonfettiView(parties = parties, modifier = Modifier.fillMaxSize())
            }
        }
    }
}