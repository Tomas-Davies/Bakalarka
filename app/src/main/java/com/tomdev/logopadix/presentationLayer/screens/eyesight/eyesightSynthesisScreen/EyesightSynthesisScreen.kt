package com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightSynthesisScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
import kotlin.math.roundToInt


class EyesightSynthesisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val app = application as com.tomdev.logopadix.LogoApp
            val levelIndex = intent.getIntExtra(IImageLevel.TAG, 0)
            val repo = app.eyesightSynthesisRepository
            val viewModel: EyesightSynthesisViewModel by viewModels {
                EyesightSynthesisViewModelFactory(repo, app, levelIndex)
            }

            AppTheme(ThemeType.THEME_EYESIGHT.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightSynthesisScreenContent(viewModel)
                }
            }
        }
    }


    @Composable
    private fun EyesightSynthesisScreenContent(viewModel: EyesightSynthesisViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            showPlaySoundIcon = true,
            soundAssignmentId = R.raw.put_the_pieces_into_image,
            viewModel = viewModel,
            title = stringResource(id = R.string.eyesight_synth_label)
        ) {
            AsyncDataWrapper(viewModel = viewModel) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EyesightSynthesisScreenRunning(viewModel = viewModel)
                }
            }
        }
    }


    /**
     *  A composable that displays the content of a running eyesight synthesis exercise,
     *  consisting of main image on top half of the screen and area of cut pieces from the image
     *  on bottom half of the screen. It manages correct positioning, scaling and initialization of
     *  every image [piece][Piece].
     *
     *  @param viewModel Provides game logic functionality.
     */
    @Composable
    private fun EyesightSynthesisScreenRunning(viewModel: EyesightSynthesisViewModel) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var imageContentScale by remember { mutableFloatStateOf(1f) }
        var imageContentOffsetInRoot by remember { mutableStateOf(Offset.Zero) }
        var imageWidth by remember { mutableFloatStateOf(0f) }
        var imageHeight by remember { mutableFloatStateOf(0f) }
        val scaledPieces by remember {
            derivedStateOf {
                uiState.pieces.map { piece ->
                    piece.copy(
                        width = (piece.width.toFloat() * imageContentScale).toInt(),
                        height = (piece.height.toFloat() * imageContentScale).toInt(),
                        bitmap = piece.bitmap
                    )
                }
            }
        }
        var initialOffsets by remember(uiState) { mutableStateOf(emptyList<Offset>()) }
        var bottomBoxOffset by remember { mutableStateOf(Offset.Zero) }

        LaunchedEffect(scaledPieces) {
            if (imageWidth > 0 && imageHeight > 0 && scaledPieces.isNotEmpty()) {
                initialOffsets = viewModel.getInitialOffsets(imageWidth, imageHeight, scaledPieces)
            }
        }
        RoundsCompletedBox(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            onExit = { finish() }
        ) {
            val imageContent = uiState.image.asImageBitmap()
            Image(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .alpha(0.3f)
                    .onGloballyPositioned { cords ->
                        imageWidth = cords.size.width.toFloat()
                        imageHeight = cords.size.height.toFloat()
                        imageContentScale =
                            getFitContentScaleInImage(imageWidth, imageHeight, imageContent)
                        val contentSize = getContentSizeInImage(imageContent, imageContentScale)
                        val contentOffset =
                            getContentOffsetInImage(contentSize, imageWidth, imageHeight)
                        imageContentOffsetInRoot = Offset(
                            cords.positionInRoot().x + contentOffset.x,
                            cords.positionInRoot().y + contentOffset.y
                        )
                    },
                bitmap = imageContent,
                contentDescription = "puzzle image"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .onGloballyPositioned { cords ->
                        bottomBoxOffset = cords.positionInRoot()
                    }
                    .align(Alignment.BottomCenter)
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                if (bottomBoxOffset != Offset.Zero) {
                    scaledPieces.forEachIndexed { index, piece ->
                        if (index < initialOffsets.size) {
                            Piece(
                                piece = piece,
                                initialOffset = initialOffsets[index],
                                bottomBoxOffset = bottomBoxOffset,
                                contentOffsetInRoot = imageContentOffsetInRoot,
                                contentScale = imageContentScale,
                                viewModel = viewModel,
                                key = uiState
                            )
                        }
                    }
                }
            }
        }
    }


    /**
     * A draggable puzzle piece composable used in the eyesight synthesis exercise.
     *
     * This composable renders a single image piece that can be dragged and placed into a target image.
     * It handles the drag gesture detection and validates correct placement of the piece.
     * When a piece is correctly placed, it triggers the appropriate callback in the viewModel and
     * becomes locked in place.
     *
     * The piece displays differently based on whether it's in the target image area:
     * - Inside image area: Normal size with no border.
     * - Outside image area: Scaled to 80% with a black border.
     *
     * @param piece Holds data about the piece.
     * @param initialOffset The starting position of the piece.
     * @param bottomBoxOffset The offset of the bottom box holding the pieces.
     * @param contentOffsetInRoot The offset of the content, inside the top image composable,
     * relative to the root composable.
     * @param contentScale The scale factor that was applied to the content inside Image composable container with ContentScale.Fit applied.
     * @param viewModel Manages the exercise state and validates
     * placements.
     * @param key A unique identifier for this piece, used to reset state when pieces are reordered.
     */
    @Composable
    private fun Piece(
        piece: ImagePiece,
        initialOffset: Offset,
        bottomBoxOffset: Offset,
        contentOffsetInRoot: Offset,
        contentScale: Float,
        viewModel: EyesightSynthesisViewModel,
        key: EyesightSynthesisUiState
    ) {
        var offset by remember { mutableStateOf(initialOffset) }
        piece.initialOffset = initialOffset

        var isDragging by remember { mutableStateOf(false) }
        var dragStarted by remember { mutableStateOf(false) }
        var dragEnded by remember { mutableStateOf(false) }
        var correctlyPlaced by remember(key) { mutableStateOf(false) }
        val width = with(LocalDensity.current) { piece.width.toDp() }
        val height = with(LocalDensity.current) { piece.height.toDp() }

        val modifier = Modifier
            .size(width, height)
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .zIndex(if (isDragging) 1f else 0f)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragEnded = false
                        dragStarted = true
                        isDragging = true
                    },
                    onDragEnd = {
                        dragStarted = false
                        dragEnded = true
                        isDragging = false
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (!correctlyPlaced) offset += dragAmount
                    }
                )
            }
            .graphicsLayer(
                alpha = if (isDragging) 0.8f else 1f
            )
        val isInImage = bottomBoxOffset.y > (offset.y + bottomBoxOffset.y)

        if (isInImage && dragEnded && !correctlyPlaced) {
            val newOffset = viewModel.setCorrectOffset(
                piece,
                bottomBoxOffset + offset,
                contentOffsetInRoot,
                contentScale,
                bottomBoxOffset
            )
            val placedWrong = piece.initialOffset
            if (newOffset != placedWrong) {
                viewModel.onCorrectlyPlaced()
                correctlyPlaced = true
            }
            dragEnded = false
            offset = newOffset
        }

        Image(
            modifier = if (isInImage)
                modifier
            else Modifier.then(
                modifier
                    .scale(0.8f)
                    .border(3.dp, Color.Black)
            ),
            bitmap = piece.bitmap.asImageBitmap(),
            contentDescription = "Image piece"
        )
    }

}