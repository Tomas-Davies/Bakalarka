package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthesisScreen

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResultBox
import com.example.bakalarkaapp.presentationLayer.components.RunningOrFinishedRoundScreen
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.utils.image.getContentOffsetInImage
import com.example.bakalarkaapp.utils.image.getFitContentScaleInImage
import com.example.bakalarkaapp.utils.image.getContentSizeInImage
import kotlin.math.roundToInt


class EyesightSynthesisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val app = application as LogoApp
            val levelIndex = intent.getIntExtra(ImageLevel.TAG, 0)

            val viewModel: EyesightSynthesisViewModel by viewModels {
                EyesightSynthesisViewModelFactory(app, levelIndex, applicationContext)
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
        val ctx = LocalContext.current
        ScreenWrapper(
            title = stringResource(id = R.string.eyesight_synth_label)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RunningOrFinishedRoundScreen(
                    viewModel = viewModel,
                    onFinish = { (ctx as Activity).finish() }
                ) {
                    EyesightSynthesisScreenRunning(viewModel = viewModel)
                }
            }
        }
    }


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

        LaunchedEffect(imageWidth, imageHeight, scaledPieces, uiState) {
            if (imageWidth > 0 && imageHeight > 0 && scaledPieces.isNotEmpty()) {
                initialOffsets = viewModel.getInitialOffsets(imageWidth, imageHeight, scaledPieces)
            }
        }

        var bottomBoxOffset by remember(uiState) { mutableStateOf(Offset.Zero) }

        AnswerResultBox(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel
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
                        imageContentScale = getFitContentScaleInImage(imageWidth, imageHeight, imageContent)
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
                                key = uiState.image.hashCode()
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun Piece(
        piece: ImagePiece,
        initialOffset: Offset,
        bottomBoxOffset: Offset,
        contentOffsetInRoot: Offset,
        contentScale: Float,
        viewModel: EyesightSynthesisViewModel,
        key: Int
    ) {
        var offset by remember { mutableStateOf(initialOffset) }
        piece.initialOffset = initialOffset

        var isDragging by remember(key) { mutableStateOf(false) }
        var dragStarted by remember(key) { mutableStateOf(false) }
        var dragEnded by remember(key) { mutableStateOf(false) }
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
                offset,
                contentOffsetInRoot,
                contentScale,
                bottomBoxOffset
            )
            val placedWrong = piece.initialOffset
            if (newOffset != placedWrong) {
                viewModel.onAllPiecesPlaced()
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