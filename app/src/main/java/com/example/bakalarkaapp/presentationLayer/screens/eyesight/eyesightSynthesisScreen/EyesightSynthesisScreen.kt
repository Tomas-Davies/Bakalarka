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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.AnswerResult
import com.example.bakalarkaapp.presentationLayer.components.ResultScreen
import com.example.bakalarkaapp.presentationLayer.states.ScreenState
import com.example.bakalarkaapp.theme.AppTheme
import kotlin.math.roundToInt


class EyesightSynthesisScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val app = application as LogoApp
            val levelIndex = intent.getIntExtra("LEVEL_INDEX", 0)

            val viewModel: EyesightSynthesisViewModel by viewModels {
                EyesightSynthesisViewModelFactory(app, levelIndex, applicationContext)
            }

            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightSynthesisScreenContent(viewModel)
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EyesightSynthesisScreenContent(viewModel: EyesightSynthesisViewModel) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_menu_label_5)) },
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
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val screenState = viewModel.screenState.collectAsState().value
                when (screenState) {
                    is ScreenState.Running -> EyesightSynthesisScreenRunning(viewModel)
                    is ScreenState.Finished -> ResultScreen(
                        viewModel.scorePercentage(),
                        onRestartBtnClick = { viewModel.restart() }
                    )
                }
            }
        }
    }


    @Composable
    private fun EyesightSynthesisScreenRunning(viewModel: EyesightSynthesisViewModel) {
        val uiState = viewModel.uiState.collectAsState().value
        var imageScale by remember { mutableFloatStateOf(1f) }
        var imageOffset by remember { mutableStateOf(Offset.Zero) }
        var containerWidth by remember { mutableIntStateOf(0) }
        var containerHeight by remember { mutableIntStateOf(0) }
        val pieces by remember {
            mutableStateOf(
                viewModel.cutImage(
                    uiState.image,
                    uiState.pieceCount
                )
            )
        }
        val scaledPieces = remember(pieces, imageScale) {
            pieces.map { piece ->
                piece.copy(
                    width = (piece.width.toFloat() * imageScale).toInt(),
                    height = (piece.height.toFloat() * imageScale).toInt(),
                    bitmap = piece.bitmap
                )
            }
        }
        var initialPositions by remember { mutableStateOf(emptyList<Offset>()) }

        LaunchedEffect(containerWidth, containerHeight, scaledPieces) {
            if (containerWidth > 0 && containerHeight > 0 && scaledPieces.isNotEmpty()) {
                val positions = mutableListOf<Offset>()

                val colCount = findSquare(scaledPieces.size)
                val rowCount = colCount
                val pieceWidth = containerWidth / colCount
                val pieceHeight = containerHeight / rowCount
                for (row in 0 until rowCount) {
                    for (col in 0 until colCount) {
                        val index = row * colCount + col
                        if (index < scaledPieces.size) {
                            val x = col * pieceWidth.toFloat()
                            val y = row * pieceHeight.toFloat()
                            positions.add(Offset(x, y))
                        }
                    }
                }
                initialPositions = positions
            }
        }

        var bottomBoxPosition by remember { mutableStateOf(Offset.Zero) }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
                val bitmap = uiState.image.asImageBitmap()
                Image(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                        .alpha(0.3f)
                        .onGloballyPositioned { cords ->
                            containerWidth = cords.size.width
                            containerHeight = cords.size.height
                            val widthScale = containerWidth.toFloat() / bitmap.width
                            val heightScale = containerHeight.toFloat() / bitmap.height
                            imageScale = minOf(widthScale, heightScale)
                            val imageX = (containerWidth - (bitmap.width * imageScale)) / 2
                            val imageY = (containerHeight - (bitmap.height * imageScale)) / 2

                            imageOffset = Offset(
                                cords.positionInRoot().x + imageX,
                                cords.positionInRoot().y + imageY
                            )
                        },
                    bitmap = bitmap,
                    contentDescription = "puzzle image"
                )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .onGloballyPositioned { cords ->
                        bottomBoxPosition = cords.positionInRoot()
                    }
                    .align(Alignment.BottomCenter)
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                if (bottomBoxPosition != Offset.Zero){
                    scaledPieces.forEachIndexed { index, piece ->
                        if (index < initialPositions.size) {
                            Piece(
                                piece = piece,
                                initialPosition = initialPositions[index],
                                bottomBoxPosition = bottomBoxPosition,
                                imagePosition = imageOffset,
                                imageScale = imageScale,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
            AnswerResult(
                viewModel = viewModel,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }


    @Composable
    private fun Piece(
        piece: ImagePiece,
        initialPosition: Offset,
        bottomBoxPosition: Offset,
        imagePosition: Offset,
        imageScale: Float,
        viewModel: EyesightSynthesisViewModel
    ) {
        var pos by remember { mutableStateOf(initialPosition) }
        piece.startingX = initialPosition.x
        piece.startingY = initialPosition.y

        var isDragging by remember { mutableStateOf(false) }
        var dragStarted by remember { mutableStateOf(false) }
        var dragEnded by remember { mutableStateOf(false) }
        val width = with(LocalDensity.current) { piece.width.toDp() }
        val height = with(LocalDensity.current) { piece.height.toDp() }

        val modifier = Modifier
            .size(width, height)
            .offset { IntOffset(pos.x.roundToInt(), pos.y.roundToInt()) }
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
                        pos += dragAmount
                    }
                )
            }
            .graphicsLayer(
                alpha = if (isDragging) 0.8f else 1f
            )
        val isInImage = bottomBoxPosition.y > (pos.y + bottomBoxPosition.y)
        if (isInImage && dragEnded){
           pos = viewModel.setCorrectPos(piece, pos, imagePosition, imageScale, bottomBoxPosition)
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

    private fun findSquare(num: Int): Int {
        var sq = 1
        while(sq * sq < num){
            sq++
        }
        return sq
    }

}