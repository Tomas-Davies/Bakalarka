package com.tomdev.logopadix.presentationLayer.screens.achievementSticker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.repositories.StickerUiModel
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.theme.AppTheme
import kotlin.getValue

class AchievementStickerScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repo = app.stickerRepo
        val viewModel: AchievementStickerViewModel by viewModels {
            AchievementStickerViewModelFactory(app, repo)
        }
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWrapper(
                        title = stringResource(R.string.achievement_sticker_heading),
                        onExit = { finish() }
                    ) { pdVal ->
                        AsyncDataWrapper(viewModel) {
                            AchievementStickerContent(viewModel, pdVal)
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun AchievementStickerContent(
        viewModel: AchievementStickerViewModel,
        pdVal: PaddingValues
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .padding(top = pdVal.calculateTopPadding(), start = 18.dp, end = 18.dp, bottom = pdVal.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.achievement_stickers_subheading),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(18.dp))
            AchievementStickers(
                viewModel = viewModel,
                uiState = uiState
            )
        }
    }


    @Composable
    fun AchievementStickers(
        viewModel: AchievementStickerViewModel,
        uiState: AchievementStickersUiState
    ){
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(uiState.stickers){ sticker ->
                Sticker(
                    viewModel = viewModel,
                    sticker = sticker
                )
            }
        }
    }


    @Composable
    fun Sticker(
        viewModel: AchievementStickerViewModel,
        sticker: StickerUiModel
    ){
        var drawableId = viewModel.getDrawableId(sticker.imageName)
        var cardColors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        var cardOutline = MaterialTheme.colorScheme.outline
        var cardOutlineSelected = MaterialTheme.colorScheme.outlineVariant

        if (sticker.collectedPieceCount >= sticker.pieceLimit){
            drawableId = viewModel.getDrawableId(sticker.imageName + "_golden")
            cardColors = CardDefaults.cardColors().copy(containerColor = colorResource(R.color.gold))
            cardOutline = colorResource(R.color.gold_outline)
            cardOutlineSelected = colorResource(R.color.gold_outline)
        }
        CustomCard(
            onClick = {},
            enabled = sticker.collectedPieceCount > 0,
            colors = cardColors,
            outlineColor = cardOutline,
            outlineSelectedColor = cardOutlineSelected
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 9.dp),
                    text = "${sticker.collectedPieceCount}/${sticker.pieceLimit}"
                )

                Image(
                    painter = painterResource(drawableId),
                    contentDescription = "sticker image"
                )
                Spacer(Modifier.height(9.dp))
                Text(
                    text = sticker.label,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(Modifier.padding(4.dp))
                Text(
                    text = sticker.description,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}